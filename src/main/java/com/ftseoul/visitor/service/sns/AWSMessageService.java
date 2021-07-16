package com.ftseoul.visitor.service.sns;

import com.ftseoul.visitor.data.Visitor;
import com.ftseoul.visitor.dto.StaffDto;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Slf4j
@Service
public class AWSMessageService implements SMSService {
    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    public CredentialService credentialService;

    private final String messageTemplate = "예약번호: ";

    private final String prefix = "+82";

    private final String suffix = "로예약 신청되었습니다.";

    public static class CredentialService {

        private final String accessKey;
        private final String secretKey;
        private final String awsRegion;


        public CredentialService(String accessKey, String secretKey, String awsRegion) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.awsRegion = awsRegion;
        }

        public AwsCredentialsProvider getAwsCredentials(String accessKey, String secretKey) {
            AwsBasicCredentials awsBasicCredentials
                = AwsBasicCredentials.create(accessKey, secretKey);
            return () -> awsBasicCredentials;
        }

        public SnsClient getSnsClient() {
            return SnsClient.builder()
                .credentialsProvider(getAwsCredentials(accessKey, secretKey))
                .region(Region.of(awsRegion))
                .build();
        }
    }

    @Override
    public void sendMessage(String phoneNumber, Long reserveId) {
        SnsClient snsClient = credentialService.getSnsClient();
        PublishRequest publishRequest = PublishRequest.builder()
            .phoneNumber(prefix.concat(phoneNumber))
            .message(messageTemplate.concat(reserveId.toString()))
            .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message status: " + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();
        log.info("Sent message Id is {}", publishResponse.messageId());
    }

    @Override
    public void sendMessage(StaffDto staffDto) {
        SnsClient snsClient = credentialService.getSnsClient();
        PublishRequest publishRequest = PublishRequest.builder()
            .phoneNumber(prefix.concat(staffDto.getPhone()))
            .message(messageTemplate.concat(staffDto.getReserveId().toString()).concat(suffix))
            .build();
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message status: " + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();
        log.info("Sent message Id is {}", publishResponse.messageId());
    }

    @Override
    public void sendMessages(List<Visitor> visitors) {
        visitors
            .forEach(visitor -> sendMessage(visitor.getPhone(), visitor.getReserveId()));
    }

    @PostConstruct
    public void createCredentialService() {
        this.credentialService = new CredentialService(this.awsAccessKey, this.awsSecretKey, this.awsRegion);
    }
}
