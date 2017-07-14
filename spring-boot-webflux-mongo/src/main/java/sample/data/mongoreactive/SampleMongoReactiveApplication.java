package sample.data.mongoreactive;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class SampleMongoReactiveApplication implements CommandLineRunner {

	@Autowired
	private ReactiveCustomerRepository repository;

	public void run(String... args) throws Exception {
		this.repository.deleteAll().then().block();

		// save a couple of customers
		this.repository.save(new Customer("Alice", "Smith")).then().block();
		this.repository.save(new Customer("Bob", "Smith")).then().block();

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		final CountDownLatch countDownLatch = new CountDownLatch(1);

		repository.findAll() //
				.doOnNext(System.out::println) //
				.doOnComplete(countDownLatch::countDown) //
				.doOnError(throwable -> countDownLatch.countDown()) //
				.subscribe();

		countDownLatch.await();
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(this.repository.findByFirstName(Mono.just("Alice")).block());

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Customer customer : this.repository.findByLastName(Mono.just("Smith"))
				.collectList().block()) {
			System.out.println(customer);
		}
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleMongoReactiveApplication.class, args);
	}
}
