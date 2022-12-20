package runners;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

public class ParallelRunner extends BlockJUnit4ClassRunner {

  public ParallelRunner(final Class<?> klass) throws InitializationError {
    super(klass);
    setScheduler(new ThreadPoll());
  }

  private static class ThreadPoll implements RunnerScheduler {

    private final ExecutorService executorService;

    public ThreadPoll() {
      this.executorService = Executors.newFixedThreadPool(5);
    }

    @Override
    public void schedule(final Runnable runnable) {
      this.executorService.submit(runnable);
    }

    @Override
    public void finished() {
      executorService.shutdown();
      try {
        executorService.awaitTermination(10, TimeUnit.MINUTES);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
