package ru.mmekhtiev.petrolconsumption;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import ru.mmekhtiev.petrolconsumption.data.Refuelling;

/**
 * Created by Mekhtievs on 17.09.2016.
 */
public class RefuellingFragment extends Fragment {
    private Refuelling mRefuelling;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mFinishedCheckBox;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRefuelling = new Refuelling();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_refueling,container,false);

        mTitleField = (EditText) v.findViewById(R.id.refuelling_title);
        mTitleField.addTextChangedListener(
                new TextWatcher(){
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mRefuelling.setTitle(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

        mDateButton = (Button)v.findViewById(R.id.refuelling_date);
        mDateButton.setText(mRefuelling.getDate().toString());
        mDateButton.setEnabled(false);

        mFinishedCheckBox = (CheckBox) v.findViewById(R.id.refuelling_finished);
        mFinishedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mRefuelling.setFinished(isChecked);
            }
        });
        return v;
    }
}
