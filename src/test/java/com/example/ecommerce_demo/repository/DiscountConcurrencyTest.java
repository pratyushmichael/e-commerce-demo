package com.example.ecommerce_demo.repository;

import com.example.ecommerce_demo.model.DiscountCode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

class DiscountConcurrencyTest {

    @Test
    void markAsUsedIfAvailable_isAtomic_onlyOneThreadSucceeds() throws InterruptedException {
        DiscountRepository repo = new DiscountRepository();
        DiscountCode discount = new DiscountCode("CONCUR10", 10, 0);
        repo.save(discount);

        int threads = 10;
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(ex.submit(() -> {
                startLatch.await();
                boolean consumed = repo.markAsUsedIfAvailable("CONCUR10");
                if (consumed) successCount.incrementAndGet();
                return consumed;
            }));
        }

        // start all threads
        startLatch.countDown();

        // wait for completion
        for (Future<Boolean> f : futures) {
            try {
                f.get(5, TimeUnit.SECONDS);
            } catch (Exception ignored) {}
        }

        ex.shutdownNow();

        // exactly one thread should have succeeded
        assertThat(successCount.get()).isEqualTo(1);
        // final state should be used
        assertThat(repo.findByCode("CONCUR10").get().isUsed()).isTrue();
    }
}
