/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package example

import kotlinx.coroutines.*
import org.apache.pulsar.client.api.*
import org.apache.pulsar.client.impl.schema.JSONSchema
import java.time.Duration


private const val SERVICE_URL = "pulsar://localhost:6650"
//private const val TOPIC_NAME = "my-property/my-ns/test-topic-jsonschema-001"
private const val TOPIC_NAME = "test-topic-jsonschema-001"
private const val SUBSCRIPTION_NAME = "test-subscription-001"
private typealias PulsarProducer = Producer<SensorReading>
private typealias PulsarConsumer = Consumer<SensorReading>

data class SensorReading(val temperature: Float)

object App {
    private val client: PulsarClient by lazy {
        PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .build()
    }

    private val producer1: PulsarProducer by lazy {
        client.newProducer(JSONSchema.of(SensorReading::class.java))
                .topic(TOPIC_NAME)
                .compressionType(CompressionType.LZ4)
                .create()
    }


    fun run() {
        println("${this::class.qualifiedName}.run()")

        launchProducerJob(producer1)
        println("producer job launched.")


        println("${this::class.qualifiedName}.run(): done.")
    }


    private fun launchProducerJob(producer: PulsarProducer): Job = runBlocking {
        GlobalScope.launch {
            while (isActive) {
                println("produce -> $SERVICE_URL/$TOPIC_NAME ...")
                (1..5).forEach { i: Int ->
                    val content = SensorReading(temperature = i.toFloat())
                    val msg = producer.newMessage()
                            .value(content)
                    //.deliverAfter(5, TimeUnit.SECONDS)
                    //.deliverAt((Instant.now() + Duration.ofDays(1)).epochSecond)

                    val messageId: MessageId = msg.send()
                    println("sent message. $messageId")
                }

                println("produce: IDLE ...")
                delay(Duration.ofSeconds(1).toMillis())
            }
        }
    }


}

fun main(args: Array<String>) {
    App.run()
}
