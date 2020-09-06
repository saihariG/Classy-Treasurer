package com.classyinc.classytreasurer.adapters;


import android.content.Context;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import com.classyinc.classytreasurer.R;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;

    private ArrayList<String> mamountList;
    private ArrayList<String> mdateList;
    private ArrayList<String> mnoteList;
    private ArrayList<String> mtitleList;
    private ArrayList<String> mtimeList;


    static class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView date,time,title,note,amount;

        SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date_txt_income_id);
            time = itemView.findViewById(R.id.time_txt_income_id);
            title = itemView.findViewById(R.id.type_txt_income_id);
            note = itemView.findViewById(R.id.note_txt_income_id);
            amount = itemView.findViewById(R.id.amoount_txt_income_id);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> mdateList, ArrayList<String> mtimeList, ArrayList<String> mtitleList, ArrayList<String> mnoteList, ArrayList<String> mamountList) {
        this.context = context;

        this.mdateList = mdateList;
        this.mtimeList = mtimeList;
        this.mtitleList = mtitleList;
        this.mnoteList = mnoteList;
        this.mamountList = mamountList;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.income_recycler_data,parent,false);
        return new SearchViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        holder.date.setText(mdateList.get(position));
        holder.time.setText(mtimeList.get(position));
        holder.title.setText(mtitleList.get(position));
        holder.note.setText(mnoteList.get(position));
        holder.amount.setText(mamountList.get(position));


    }

    @Override
    public int getItemCount() {
        return mtitleList.size();
    }
}

