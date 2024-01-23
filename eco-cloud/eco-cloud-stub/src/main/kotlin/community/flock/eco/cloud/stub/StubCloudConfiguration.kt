package community.flock.eco.cloud.stub

import community.flock.eco.cloud.stub.services.StubMailService
import community.flock.eco.cloud.stub.services.StubStoreService
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(StubMailService::class, StubStoreService::class)
class StubCloudConfiguration
