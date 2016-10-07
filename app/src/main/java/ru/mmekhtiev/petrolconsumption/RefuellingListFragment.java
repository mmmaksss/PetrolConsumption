package ru.mmekhtiev.petrolconsumption;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import ru.mmekhtiev.petrolconsumption.data.Refuelling;
import ru.mmekhtiev.petrolconsumption.data.RefuellingLab;

public class RefuellingListFragment extends Fragment {

    private RecyclerView mRefuellingRecycleView;
    private RefuellingAdapter mRefuellingAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refuelling_list,container,false);
        mRefuellingRecycleView = (RecyclerView) view.findViewById(R.id.refuelling_recycler_view);
        mRefuellingRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        RefuellingLab refuellingLab = RefuellingLab.get(getActivity());
        List<Refuelling> refuellings = refuellingLab.getRefuellings();

        if (mRefuellingAdapter != null) {
            mRefuellingAdapter.notifyDataSetChanged();
        } else {
            mRefuellingAdapter = new RefuellingAdapter(refuellings);
            mRefuellingRecycleView.setAdapter(mRefuellingAdapter);
        }
    }

    private class RefuellingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Refuelling mRefuelling;

        public RefuellingHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_solved_check_box);
            itemView.setOnClickListener(this);


        }

        public void bindRefuelling(Refuelling refuelling) {
            mRefuelling = refuelling;
            mTitleTextView.setText(mRefuelling.getTitle());
            mDateTextView.setText(mRefuelling.getDate().toString());
            mSolvedCheckBox.setChecked(mRefuelling.isFinished());
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mRefuelling.setFinished(isChecked);
                }
            });
        }

        @Override
        public void onClick(View v) {
            startActivity(RefuellingActivity.newIntent(getActivity(),mRefuelling.getId()));
        }
    }

    private class RefuellingAdapter extends RecyclerView.Adapter<RefuellingHolder> {

        private List<Refuelling> mRefuellings;

        public RefuellingAdapter(List<Refuelling> refuellings) {
            mRefuellings = refuellings;
        }

        @Override
        public RefuellingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_refuelling,parent,false);
            return new RefuellingHolder(view);
        }

        @Override
        public void onBindViewHolder(RefuellingHolder holder, int position) {
            Refuelling refuelling = mRefuellings.get(position);
            holder.bindRefuelling(refuelling);
        }

        @Override
        public int getItemCount() {
            return mRefuellings.size();
        }
    }
}
