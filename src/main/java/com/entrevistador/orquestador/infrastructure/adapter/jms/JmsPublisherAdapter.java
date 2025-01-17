package com.entrevistador.orquestador.infrastructure.adapter.jms;

import com.entrevistador.orquestador.dominio.model.dto.*;
import com.entrevistador.orquestador.dominio.model.dto.SolicitudHojaDeVidaDto;
import com.entrevistador.orquestador.dominio.port.jms.JmsPublisherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public final class JmsPublisherAdapter implements JmsPublisherClient {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic-analizador-publisher}")
    private String hojaDeVidaPublisherTopic;

    @Value("${kafka.topic-recopilador-publisher}")
    private String topicEmpresa;

    @Value("${kafka.topic-generador-publisher}")
    private String topicGenerador;

    @Value("${kafka.topic-analizador-validador-publisher}")
    private String topicValidador;

    @Override
    public Mono<Void> enviarHojaDeVida(SolicitudHojaDeVidaDto solicitudHojaDeVidaDto) {

        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(hojaDeVidaPublisherTopic, solicitudHojaDeVidaDto);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + solicitudHojaDeVidaDto.getUsername() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            solicitudHojaDeVidaDto.toString() + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }

        return Mono.empty();
    }

    @Override
    public Mono<Void> enviarInformacionEmpresa(PosicionEntrevistaDto perfil) {

        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicEmpresa, perfil);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + perfil.getIdEntrevista() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            perfil.toString() + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }

        return Mono.empty();
    }

    @Override
    public Mono<Void> generarEntrevista(SolicitudGeneracionEntrevistaDto solicitudGeneracionEntrevista) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicGenerador, solicitudGeneracionEntrevista);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + solicitudGeneracionEntrevista.getIdEntrevista() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            solicitudGeneracionEntrevista.toString() + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }

        return Mono.empty();
    }

    @Override
    public Mono<Void> validarmatchHojaDeVida(SolicitudMatchDto solicitudMatchDto) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topicValidador, solicitudMatchDto);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    System.out.println("Sent message=[" + solicitudMatchDto.getIdEntrevista() +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" +
                            solicitudMatchDto.toString() + "] due to : " + ex.getMessage());
                }
            });

        } catch (Exception ex) {
            System.out.println("ERROR : " + ex.getMessage());
        }

        return Mono.empty();
    }
}
