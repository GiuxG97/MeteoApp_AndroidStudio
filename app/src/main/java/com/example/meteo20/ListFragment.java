package com.example.meteo20;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListFragment extends Fragment {
    public static RecyclerView mCrimeRecyclerView;
    public static EntryAdapter mAdapter;
    public static List<City> entries;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        entries = EntriesHolder.get(getActivity()).getEntries();
        mAdapter = new EntryAdapter(entries);
        mCrimeRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter == null) {
        } else
            mAdapter.notifyDataSetChanged();
    }

    public static void metodoAggiorna() {
        mAdapter.notifyDataSetChanged();
    }

    private class EntryHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnTaskCompletedFlag {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView a;
        private City mCity;

        public EntryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.title);
            mDateTextView = itemView.findViewById(R.id.date);
            a = itemView.findViewById(R.id.imageView3);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailPagerActivity.newIntent(getActivity(), mCity.getId());
            DetailPagerActivity.city = mCity;
            startActivity(intent);
        }

        public void bind(City city) {
            mCity = city;
            mTitleTextView.setText(((mCity.getmCity().substring(0, 1).toUpperCase() + mCity.getmCity().substring(1).toLowerCase()) + ", " + mCity.getmNation()));
            mDateTextView.setText(mCity.getDate().toString());
            TaskRequestFlag f = new TaskRequestFlag(this);
            f.execute(mCity.getmNation());
        }

        @Override
        public void onTaskCompleted(Drawable items) {
            a.setImageDrawable(items);
        }
    }

    private class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {
        private List<City> mEntries;

        public EntryAdapter(List<City> crimes) {
            mEntries = crimes;
        }

        @Override
        public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EntryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EntryHolder holder, int position) {
            City city = mEntries.get(position);
            holder.bind(city);
        }

        @Override
        public int getItemCount() {
            return mEntries.size();
        }

    }
}
