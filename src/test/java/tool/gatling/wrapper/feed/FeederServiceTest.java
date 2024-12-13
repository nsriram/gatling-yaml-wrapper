package tool.gatling.wrapper.feed;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.FeederBuilder;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class FeederServiceTest {
    @Test
    public void shouldReturnChainBuilderForFeedFile() {
        String feedFile = "simple/data-feed.csv";
        FeederBuilder.Batchable.Impl<String> mockFeedBatchable = mock(FeederBuilder.Batchable.Impl.class);
        FeederBuilder.Batchable<String> mockFeedBuilder = mock(FeederBuilder.Batchable.class);
        when(mockFeedBatchable.circular()).thenReturn(mockFeedBuilder);
        ChainBuilder mockChainBuilder = mock(ChainBuilder.class);

        try (MockedStatic<CoreDsl> mockCoreDsl = Mockito.mockStatic(CoreDsl.class)) {
            mockCoreDsl.when(() -> CoreDsl.csv(feedFile)).thenReturn(mockFeedBatchable);
            mockCoreDsl.when(() -> CoreDsl.feed(mockFeedBuilder)).thenReturn(mockChainBuilder);

            ChainBuilder chainBuilder = FeederService.feedChain(feedFile);

            mockCoreDsl.verify(() -> CoreDsl.csv(feedFile), times(1));
            verify(mockFeedBatchable, times(1)).circular();

            assertNotNull(chainBuilder);
            assertSame(mockChainBuilder, chainBuilder);
        }
    }
}