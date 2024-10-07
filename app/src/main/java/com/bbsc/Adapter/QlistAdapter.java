package com.bbsc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bbsc.Activity.MainActivity;
import com.bbsc.Model.GetDetail;
import com.bbsc.Model.QlistRes;
import com.bbsc.Model.Quiz;
import com.bbsc.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class QlistAdapter extends RecyclerView.Adapter<QlistAdapter.ViewHolder> {

    Context context;
    List<QlistRes.Ques> ques;
    RadioGroup.LayoutParams rprms;
    int checkedposition;
    MainActivity mn;
    public QlistAdapter(Context mContext, List<QlistRes.Ques> ques) {
        this.context = mContext;
        this.ques = ques;

        Log.d("ques_size", String.valueOf(ques.size()));
    }

    @NonNull
    @Override
    public QlistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_qlist, viewGroup, false);
        return new ViewHolder(view);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull QlistAdapter.ViewHolder holder, int position) {
        int actualPosition = holder.getAdapterPosition();  // Fetch the latest adapter position
        holder.srno.setText((actualPosition + 1) + "/" + ques.size());
        QlistRes.Ques current_que = ques.get(actualPosition);

        holder.ClearChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.optionRadioGroup.clearCheck();
            }
        });

        mn = new MainActivity();

        holder.Que.setText(Html.fromHtml(Html.fromHtml(current_que.getQueTitle()).toString()));
        holder.setIsRecyclable(false);

        try {
            JSONArray jsonArray = new JSONArray(current_que.getOptions());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                boolean ischecked = actualPosition == checkedposition;

                String X = jsonObject.getString(String.valueOf((i + 1)));
                Log.d("X loop", X);
                RadioButton radioButton = new RadioButton(context);
                JSONObject getarray = new JSONObject(GetDetail.att_data.get(actualPosition).toString());
                int opt = (i + 1);
                radioButton.setText(X);
                radioButton.setId(opt);

                if (getarray.get("stud_given_ans").equals(opt)) {
                    radioButton.setChecked(true);
                }

                radioButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
                        int sel = holder.optionRadioGroup.getCheckedRadioButtonId();

                        try {
                            JSONObject jsonObject = new JSONObject(GetDetail.att_data.get(holder.getAdapterPosition()).toString());
                            jsonObject.put("stud_given_ans", sel);
                            jsonObject.put("correct_ans", current_que.getCorrectAns());

                            if (current_que.getCorrectAns().equals(String.valueOf(sel))) {
                                jsonObject.put("is_correct", "1");
                                jsonObject.put("ans_marks", current_que.getQueMarks());
                            } else {
                                jsonObject.put("is_correct", "0");
                                jsonObject.put("ans_marks", "0");
                            }

                            GetDetail.att_data.put(holder.getAdapterPosition(), jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                rprms = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                holder.optionRadioGroup.addView(radioButton, rprms);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindViewHolder(@NonNull QlistAdapter.ViewHolder holder, int position) {
//            holder.srno.setText((position+1)+"/"+ques.size());
//        QlistRes.Ques current_que = ques.get(position);
//        holder.ClearChk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.optionRadioGroup.clearCheck();
//            }
//        });
//
//        mn = new MainActivity();
//            /*switch (current_que.getQueType()){
//                case "regular": holder.Qtype.setText("Single Choice"); break;
//                case "multiple": holder.Qtype.setText("Multiple Choice"); break;
//                default: holder.Qtype.setText(""); break;
//
//            }*/
//
//            holder.Que.setText(Html.fromHtml(Html.fromHtml(current_que.getQueTitle()).toString()));
//        holder.setIsRecyclable(false);
//
//        try {
//            JSONArray jsonArray = new JSONArray(current_que.getOptions());
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                boolean ischecked = position == checkedposition ? true : false;
//
//                String X = jsonObject.getString(String.valueOf((i+1)));
//                Log.d("X loop", X);
//                RadioButton radioButton = new RadioButton(context);
//                JSONObject getarray = new JSONObject(GetDetail.att_data.get(position).toString());
//                int opt = (i+1);
//                radioButton.setText(X);
//                radioButton.setId(opt);
////                Log.d("stud_given_ans** ", String.valueOf(getarray.get("stud_given_ans"))+ (i+1));
//
//                if(getarray.get("stud_given_ans").equals(opt)) {
//                    radioButton.setChecked(true);
////                    Log.d("i+1", String.valueOf(opt));
//                }
//
//
//                radioButton.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("ResourceType")
//                    @Override
//                    public void onClick(View view) {
//                        Log.d("attempt_prev11", new Gson().toJson(GetDetail.att_data));
//                        if(radioButton.isChecked()){
//                            Log.d("sel", "seleteddd");
//
//                        }
//                            int sel = holder.optionRadioGroup.getCheckedRadioButtonId();
//
//                        try {
//
//                            JSONObject jsonObject = new JSONObject(GetDetail.att_data.get(position).toString());
//
////                            GetDetail.att_data.get(position);
//                            jsonObject.put("stud_given_ans", sel);
//                            jsonObject.put("correct_ans", current_que.getCorrectAns());
//
//                            if(current_que.getCorrectAns().equals(String.valueOf(sel))) {
//                                jsonObject.put("is_correct", "1");
//                                jsonObject.put("ans_marks", current_que.getQueMarks());
//                            }
//                            else{
//                                jsonObject.put("is_correct", "0");
//                                jsonObject.put("ans_marks", "0");
//
//
//                            }
//                            GetDetail.att_data.put(position, jsonObject);
////                            Log.d("attempt_after", new Gson().toJson(GetDetail.att_data));
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
////                        Log.d("selbtn", String.valueOf(sel+" position: "+position));
//                    }
//                });
//
//                rprms = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                holder.optionRadioGroup.addView(radioButton, rprms);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public int getItemCount() {
        return ques.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView srno, Que, Qtype, ClearChk;
        RadioGroup optionRadioGroup;

        public ViewHolder(View view) {
            super(view);
            srno = view.findViewById(R.id.srno);
            Que = view.findViewById(R.id.Que);
            ClearChk = view.findViewById(R.id.clearChk);
//            Qtype = view.findViewById(R.id.Qtype);
            optionRadioGroup = view.findViewById(R.id.optionRadioGroup);
        }
    }
}
