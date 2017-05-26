package rssdemo.andras.hu.rssdemo.di;

import rssdemo.andras.hu.rssdemo.network.FeedApi;
import rssdemo.andras.hu.rssdemo.repository.FeedConverter;
import rssdemo.andras.hu.rssdemo.repository.FeedRepository;
import rssdemo.andras.hu.rssdemo.ui.FeedActivity;

/**
 * I don't like Dagger and this simple class is enough to represent di.
 */
public class Injector {

    private static FeedRepository feedRepository = new FeedRepository(new FeedConverter(new FeedApi()));

    public static void inject(FeedActivity activity) {
        activity.feedRepository = feedRepository;
    }
}
