package rssdemo.andras.hu.rssdemo.ui.subscriptions.editor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rssdemo.andras.hu.rssdemo.data.Subscription;
import rssdemo.andras.hu.rssdemo.databinding.ViewSubscriptionEditorBinding;
import rssdemo.andras.hu.rssdemo.di.Injector;


public class SubscriptionEditorDialogFragment extends DialogFragment implements SubscriptionEditorView{

    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "url";

    public SubscriptionEditorViewModel viewModel;
    private TextInputLayoutValidationHelper nameValidatorHelper;
    private TextInputLayoutValidationHelper urlValidatorHelper;
    private ViewSubscriptionEditorBinding binding;

    public static SubscriptionEditorDialogFragment create(Subscription subscription) {
        SubscriptionEditorDialogFragment fragment = new SubscriptionEditorDialogFragment();
        Injector.inject(fragment);
        if (subscription != null) {
            Bundle args = new Bundle();
            args.putString(KEY_NAME, subscription.getName());
            args.putString(KEY_URL, subscription.getUrl());
            fragment.setArguments(args);
        }
        return fragment;
    }

    public static SubscriptionEditorDialogFragment create() {
        return create(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ViewSubscriptionEditorBinding.inflate(inflater, container, false);
        Subscription subscription = new Subscription();
        String name = null;
        if (getArguments() != null) {
            name = getArguments().getString(KEY_NAME);
            subscription.setName(name);
            subscription.setUrl(getArguments().getString(KEY_URL));
        }
        viewModel.setView(this, name);
        binding.setSubscription(subscription);
        binding.setHandler(viewModel);
        nameValidatorHelper = new TextInputLayoutValidationHelper(getContext(), binding.tilName);
        urlValidatorHelper = new TextInputLayoutValidationHelper(getContext(), binding.tilUrl);
        return binding.getRoot();
    }

    @Override
    public void raiseErrorOnName(@StringRes int messageResId) {
        nameValidatorHelper.raiseError(messageResId);
    }

    @Override
    public void raiseErrorOnUrl(@StringRes int messageResId) {
        urlValidatorHelper.raiseError(messageResId);
    }

    @Override
    public void closeEditor() {
        getDialog().dismiss();
    }

    @Override
    public Subscription getSubscrition() {
        return new Subscription(binding.name.getText().toString(), binding.url.getText().toString());
    }
}