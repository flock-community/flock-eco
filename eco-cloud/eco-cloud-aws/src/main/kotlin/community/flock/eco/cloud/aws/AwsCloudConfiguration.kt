package community.flock.eco.cloud.aws

import community.flock.eco.cloud.aws.services.AwsMailService
import community.flock.eco.cloud.aws.services.AwsStorageService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(AwsStorageService::class, AwsMailService::class)
class AwsCloudConfiguration
