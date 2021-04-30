package com.mongodb.redis.integration.initialize;

import com.mongodb.redis.integration.document.Item;
import com.mongodb.redis.integration.repository.ItemReactiveRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class ItemDataInitiliazer implements CommandLineRunner {

  private final ItemReactiveRepository itemReactiveRepository;

  @Override
  public void run(String... args) throws Exception {
    initialDataSetUp();
  }

  private void initialDataSetUp() {
    this.itemReactiveRepository
        .deleteAll()
        .thenMany(Flux.fromIterable(getItemsList()))
        .flatMap(this.itemReactiveRepository::save)
        .thenMany(this.itemReactiveRepository.findAll())
        .subscribe(item -> log.info("Items Inserted from CommandlineRunner : {}", item));
  }

  public static List<Item> getItemsList() {
    return List.of(
        new Item(null, "Samsung TV", 399.99),
        new Item(null, "LG TV", 4200.0),
        new Item(null, "Apple Watch", 9000.99),
        new Item(null, "Bose Headphone", 900.99));
  }
}
