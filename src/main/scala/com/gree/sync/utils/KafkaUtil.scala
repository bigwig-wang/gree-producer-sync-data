package com.gree.sync.utils

import java.util.Properties

import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

@Singleton
class KafkaUtil {

  @Inject
  @Named("kafka.brokers")
  var brokerList: String = _

  @Inject
  @Named("kafka.security.protocol")
  var kafkaSecurityProtocol: String = _

  @Inject
  @Named("kafka.login.config.location")
  var kafkaLoginConfigLocation: String = _

  @Inject
  @Named("kerberos.krb5.location")
  var krb5Location: String = _

  @Inject
  @Named("kafka.sasl.kerberos.service.name")
  var kafkaServiceName: String = _

  var kafkaProps: Properties = _

  var kafkaProducer: KafkaProducer[String, String] = _

  def kafkaProducerConfigs(): Properties = {
    if (kafkaProps == null) {
      val props = new Properties()
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList)
      props.put(ProducerConfig.ACKS_CONFIG, "all")
      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, kafkaSecurityProtocol)
      props.put("sasl.kerberos.service.name", kafkaServiceName)

      System.setProperty("java.security.krb5.conf", krb5Location)
      System.setProperty("java.security.auth.login.config", kafkaLoginConfigLocation)
      System.setProperty("javax.security.auth.useSubjectCredsOnly", "false")
      kafkaProps = props
    }
    kafkaProps
  }

  def getProducer(): KafkaProducer[String, String] = {
    if (kafkaProducer == null)
      kafkaProducer = new KafkaProducer[String, String](kafkaProducerConfigs())
    kafkaProducer
  }

  def produce(topic: String, messages: String): Unit = {
    getProducer().send(new ProducerRecord[String, String](topic, messages))
  }

}
