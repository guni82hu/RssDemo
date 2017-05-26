package rssdemo.andras.hu.rssdemo.ui;


import java.util.List;

import rssdemo.andras.hu.rssdemo.data.Subscription;

public interface FeedView {
    FeedAdapter getAdapter();

    void populateDrawerMenu(List<Subscription> subscriptions);

    void showError();
}
