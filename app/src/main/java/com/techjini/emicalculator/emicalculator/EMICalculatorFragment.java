package com.techjini.emicalculator.emicalculator;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.techjini.emicalculator.R;
import com.techjini.emicalculator.databinding.FragmentEmiCalculatorBinding;


public class EMICalculatorFragment extends Fragment implements EMICalculatorContract.View,
        Spinner.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener, EditText.OnFocusChangeListener,
        View.OnTouchListener, EditText.OnKeyListener, EditText.OnEditorActionListener {

    private EMICalculatorPresenter mPresenter;
    private EMICalculatorModel mEmicalculatorModel;
    private long mStartClickTime;
    private boolean isOnTouch;
    protected ViewDataBinding mBinding;

    public static EMICalculatorFragment getInstance() {
        EMICalculatorFragment aboutUsFragment = new EMICalculatorFragment();
        return aboutUsFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(false);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_emi_calculator, container, false);
        mPresenter = new EMICalculatorPresenter(this);
        intializeViews();
        getPresenter().formLoanTypeObjects(0);
        return mBinding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void intializeViews() {
        getBinding().loanSeekbar.setOnSeekBarChangeListener(this);
        getBinding().tenureSeekbar.setOnSeekBarChangeListener(this);
        getBinding().roiSeekbar.setOnSeekBarChangeListener(this);
        getBinding().selectedRoi.setOnFocusChangeListener(this);
        getBinding().selectedTenure.setOnFocusChangeListener(this);
        getBinding().selectedLoanAmount.setOnFocusChangeListener(this);
        getBinding().loanLayout.setOnTouchListener(this);
        getBinding().tenureLayout.setOnTouchListener(this);
        getBinding().roiLayout.setOnTouchListener(this);
        getBinding().selectedTenure.setOnKeyListener(this);
        getBinding().selectedLoanAmount.setOnKeyListener(this);
        getBinding().selectedRoi.setOnEditorActionListener(this);
    }


    private FragmentEmiCalculatorBinding getBinding() {
        return (FragmentEmiCalculatorBinding) mBinding;
    }


    @Override
    public void populateLoanType(EMICalculatorModel emiCalculatorModel) {
        isOnTouch = false;
        mEmicalculatorModel = emiCalculatorModel;
        getBinding().setObj(emiCalculatorModel);
        getBinding().loanSeekbar.setProgress(mEmicalculatorModel.getMinLoanAmountValue());
        getBinding().tenureSeekbar.setProgress(mEmicalculatorModel.getMinTenure());
        getBinding().roiSeekbar.setProgress(mEmicalculatorModel.getMinROI());
        getBinding().roiSeekbar.setMax(((mEmicalculatorModel.getMaxROI() * 10)));
        getBinding().loanSeekbar.setMax(((mEmicalculatorModel.getMaxLaonAmountValue() / mEmicalculatorModel.getLoanAmountInterval())));
        getBinding().tenureSeekbar.setMax(mEmicalculatorModel.getMaxTenure() / mEmicalculatorModel.getTenureInterval());

    }

    @Override
    public void updateMonthlyEMI() {
        isOnTouch = false;
    }

    private EMICalculatorPresenter getPresenter() {
        return (EMICalculatorPresenter) mPresenter;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        removeFocus();
        getPresenter().formLoanTypeObjects(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        switch (seekBar.getId()) {
            case R.id.loan_seekbar: {

                int seekbarPosition = (i / mEmicalculatorModel.getLoanAmountInterval()) * mEmicalculatorModel.getLoanAmountInterval();
                //  double seekbarPosition=i*mEmicalculatorModel.getLoanAmountInterval();
                if (seekbarPosition == 0 || seekbarPosition <= mEmicalculatorModel.getMinLoanAmountValue()) {
                    seekbarPosition = mEmicalculatorModel.getMinLoanAmountValue();
                    getBinding().loanSeekbar.setProgress(0);
                } else {
                    getBinding().loanSeekbar.setProgress((seekbarPosition));
                }
                mEmicalculatorModel.setSelectedLoanAmount(seekbarPosition);
                getPresenter().calculateMonthlyEMI(mEmicalculatorModel);
                break;
            }
            case R.id.tenure_seekbar: {
                //  double seekbarPosition = (i / mEmicalculatorModel.getTenureInterval()) * mEmicalculatorModel.getTenureInterval();
                int seekbarPosition = i * mEmicalculatorModel.getTenureInterval();
                if (seekbarPosition == 0 || seekbarPosition <= mEmicalculatorModel.getMinTenure()) {
                    seekbarPosition = mEmicalculatorModel.getMinTenure();
                    getBinding().tenureSeekbar.setProgress(0);
                } else {
                    getBinding().tenureSeekbar.setProgress(seekbarPosition / mEmicalculatorModel.getTenureInterval());
                }
                mEmicalculatorModel.setSelectedTenure(seekbarPosition);
                getPresenter().calculateMonthlyEMI(mEmicalculatorModel);

                break;
            }
            case R.id.roi_seekbar: {
                double seekbarPosition = ((double) i / 10);
                if (seekbarPosition == 0 || seekbarPosition <= mEmicalculatorModel.getMinROI()) {
                    seekbarPosition = mEmicalculatorModel.getMinROI();
                    getBinding().roiSeekbar.setProgress(0);
                } else {
                    getBinding().roiSeekbar.setProgress((int) (seekbarPosition * 10));
                }
                mEmicalculatorModel.setSelectedROI(seekbarPosition);
                getPresenter().calculateMonthlyEMI(mEmicalculatorModel);
                break;
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {


    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!isOnTouch) {
            switch (view.getId()) {
                case R.id.selected_loan_amount: {
                    if (!b) {
                        if (TextUtils.isEmpty(getBinding().selectedLoanAmount.getText().toString())) {
                            getBinding().loanSeekbar.setProgress(0);
                        } else {

                            if (getPresenter().isInRange(mEmicalculatorModel.getMinLoanAmountValue(), mEmicalculatorModel.getMaxLaonAmountValue(), (double) (mEmicalculatorModel.getLoanAmountInterval()), getBinding().selectedLoanAmount.getText().toString())) {
                                if (getPresenter().isValidMultiple(mEmicalculatorModel.getMinLoanAmountValue(), mEmicalculatorModel.getMaxLaonAmountValue(), (double) mEmicalculatorModel.getLoanAmountInterval(), getBinding().selectedLoanAmount.getText().toString())) {
                                    getBinding().loanSeekbar.setProgress((int) (Double.parseDouble(getBinding().selectedLoanAmount.getText().toString()) * mEmicalculatorModel.getLoanAmountInterval() / mEmicalculatorModel.getLoanAmountInterval()));
                                    getPresenter().calculateMonthlyEMI(mEmicalculatorModel);
                                }
                            }
                        }
                    }
                    break;
                }
                case R.id.selected_tenure: {
                    if (!b) {
                        if (TextUtils.isEmpty(getBinding().selectedTenure.getText().toString())) {
                            getBinding().tenureSeekbar.setProgress(0);
                        } else {

                            if (getPresenter().isInRange(mEmicalculatorModel.getMinTenure(), mEmicalculatorModel.getMaxTenure(), (double) mEmicalculatorModel.getTenureInterval(), getBinding().selectedTenure.getText().toString())) {
                                if (getPresenter().isValidMultiple(mEmicalculatorModel.getMinTenure(), mEmicalculatorModel.getMaxTenure(), (double) mEmicalculatorModel.getTenureInterval(), getBinding().selectedTenure.getText().toString())) {
                                    getBinding().tenureSeekbar.setProgress((int) (Double.parseDouble(getBinding().selectedTenure.getText().toString()) / mEmicalculatorModel.getTenureInterval()));
                                    getPresenter().calculateMonthlyEMI(mEmicalculatorModel);
                                }
                            }
                        }
                    }
                    break;
                }
                case R.id.selected_roi: {
                    if (!b) {
                        if (TextUtils.isEmpty(getBinding().selectedRoi.getText().toString())) {
                            getBinding().roiSeekbar.setProgress(0);
                        } else {

                            if (getPresenter().isInRange(mEmicalculatorModel.getMinROI(), mEmicalculatorModel.getMaxROI(), mEmicalculatorModel.getROIInterval(), getBinding().selectedRoi.getText().toString())) {
                                if (getPresenter().isValidMultiple(mEmicalculatorModel.getMinROI(), mEmicalculatorModel.getMaxROI(), mEmicalculatorModel.getROIInterval(), getBinding().selectedRoi.getText().toString())) {
                                    getBinding().roiSeekbar.setProgress((int) (Double.parseDouble(getBinding().selectedRoi.getText().toString()) * 10));
                                    getPresenter().calculateMonthlyEMI(mEmicalculatorModel);
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private void validateROI() {
            if (TextUtils.isEmpty(getBinding().selectedRoi.getText().toString())) {
                getBinding().roiSeekbar.setProgress(0);
            } else {
                if (getPresenter().isInRange(mEmicalculatorModel.getMinROI(), mEmicalculatorModel.getMaxROI(), mEmicalculatorModel.getROIInterval(), getBinding().selectedRoi.getText().toString())) {
                    if (getPresenter().isValidMultiple(mEmicalculatorModel.getMinROI(), mEmicalculatorModel.getMaxROI(), mEmicalculatorModel.getROIInterval(), getBinding().selectedRoi.getText().toString())) {
                        getBinding().roiSeekbar.setProgress((int) (Double.parseDouble(getBinding().selectedRoi.getText().toString()) * 10));
                        getPresenter().calculateMonthlyEMI(mEmicalculatorModel);
                    }
                }

            }
        }

    private void validateTenure() {
        if (TextUtils.isEmpty(getBinding().selectedTenure.getText().toString())) {
            getBinding().tenureSeekbar.setProgress(0);
        } else {
            if (getPresenter().isInRange(mEmicalculatorModel.getMinTenure(), mEmicalculatorModel.getMaxTenure(), (double) mEmicalculatorModel.getTenureInterval(), getBinding().selectedTenure.getText().toString())) {
                if (getPresenter().isValidMultiple(mEmicalculatorModel.getMinTenure(), mEmicalculatorModel.getMaxTenure(), (double) mEmicalculatorModel.getTenureInterval(), getBinding().selectedTenure.getText().toString())) {
                    getBinding().tenureSeekbar.setProgress((int) (Double.parseDouble(getBinding().selectedTenure.getText().toString()) / mEmicalculatorModel.getTenureInterval()));
                    validateROI();
                }
            }
        }
    }

    private void validateLoanAmount() {
        if (TextUtils.isEmpty(getBinding().selectedLoanAmount.getText().toString())) {
            getBinding().loanSeekbar.setProgress(0);
        } else {

            if (getPresenter().isInRange(mEmicalculatorModel.getMinLoanAmountValue(), mEmicalculatorModel.getMaxLaonAmountValue(), (double) (mEmicalculatorModel.getLoanAmountInterval()), getBinding().selectedLoanAmount.getText().toString())) {
                if (getPresenter().isValidMultiple(mEmicalculatorModel.getMinLoanAmountValue(), mEmicalculatorModel.getMaxLaonAmountValue(), (double) mEmicalculatorModel.getLoanAmountInterval(), getBinding().selectedLoanAmount.getText().toString())) {
                    getBinding().loanSeekbar.setProgress((int) (Double.parseDouble(getBinding().selectedLoanAmount.getText().toString()) * mEmicalculatorModel.getLoanAmountInterval() / mEmicalculatorModel.getLoanAmountInterval()));
                    validateTenure();
                }


            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int MAX_CLICK_DURATION = 200;
        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:

            {
                mStartClickTime = java.util.Calendar.getInstance().getTimeInMillis();
                break;
            }
            case MotionEvent.ACTION_UP: {
                long clickDuration = java.util.Calendar.getInstance().getTimeInMillis() - mStartClickTime;
                if (clickDuration < MAX_CLICK_DURATION) {
                    removeFocus();
                    CommonUtils.hideSoftKeyboard(getActivity());
                    validateLoanAmount();
                }
            }
        }
        return true;

    }

    private void removeFocus() {
        isOnTouch = true;
        if (getBinding().selectedLoanAmount.hasFocus()) {
            getBinding().selectedLoanAmount.clearFocus();
        } else if (getBinding().selectedTenure.hasFocus()) {
            getBinding().selectedTenure.clearFocus();
        } else if (getBinding().selectedRoi.hasFocus()) {
            getBinding().selectedRoi.clearFocus();
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (view.getId() == R.id.selected_roi && (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
            CommonUtils.hideSoftKeyboard(getActivity());
            validateLoanAmount();
            return true;
        } else if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
            validateLoanAmount();
            return true;
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_GO) {
            CommonUtils.hideSoftKeyboard(getActivity());
            validateLoanAmount();
            return true;
        }
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onViewActive(this);

    }

}
