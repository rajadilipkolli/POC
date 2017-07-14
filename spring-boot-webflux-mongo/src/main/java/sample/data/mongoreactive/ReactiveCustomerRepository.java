package sample.data.mongoreactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveCustomerRepository extends ReactiveMongoRepository<Customer, String> {

	Mono<Customer> findByFirstName(Mono<String> firstName);
	
	Flux<Customer> findByLastName(Mono<String> lastName);
}
