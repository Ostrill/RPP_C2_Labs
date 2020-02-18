package com.vladimir.rpp_lab_1;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private final String TAG = "ListAdapter";

    private List<Integer> list = new ArrayList<>();

    public void setList() {
        for (int i = 1; i <= 1000000; ++i) {
            list.add(i);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.view_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String number = numberToString(list.get(position));

        holder.text.setText(number);
        holder.layout.setBackgroundColor((position % 2 == 1) ? Color.LTGRAY : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String subThousand(int rank, int num) {
        String string = "";
        switch (rank % 3) {
            case 0:
                if ((num % 100) / 10 != 1) {
                    switch (num % 10) {
                        case 1: return (rank == 3) ? "одна " : "один " ;
                        case 2: return (rank == 3) ? "две "  : "два "  ;
                        case 3: return "три "   ;
                        case 4: return "четыре ";
                        case 5: return "пять "  ;
                        case 6: return "шесть " ;
                        case 7: return "семь "  ;
                        case 8: return "восемь ";
                        case 9: return "девять ";
                    }
                } else {
                    switch (num % 10) {
                        case 1: return "одиннадцать " ;
                        case 2: return "двенадцать "  ;
                        case 3: return "тринадцать "  ;
                        case 4: return "четырнадцать ";
                        case 5: return "пятнадцать "  ;
                        case 6: return "шестнадцать " ;
                        case 7: return "семнадцать "  ;
                        case 8: return "восемнадцать ";
                        case 9: return "девятнадцать ";
                    }
                }
                break;
            case 1:
                switch (num % 10) {
                    case 1: return "десять "     ;
                    case 2: return "двадцать "   ;
                    case 3: return "тридцать "   ;
                    case 4: return "сорок "      ;
                    case 5: return "пятьдесят "  ;
                    case 6: return "шестьдесят " ;
                    case 7: return "семьдесят "  ;
                    case 8: return "восемьдесят ";
                    case 9: return "девяносто "  ;
                } break;
            case 2:
                switch (num % 10) {
                    case 1: return "сто "      ;
                    case 2: return "двести "   ;
                    case 3: return "триста "   ;
                    case 4: return "четыреста ";
                    case 5: return "пятьсот "  ;
                    case 6: return "шестьсот " ;
                    case 7: return "семьсот "  ;
                    case 8: return "восемьсот ";
                    case 9: return "девятьсот ";
                } break;
        }

        return string;
    }

    private String numberToString(int number) {
        if (number == 1000000) {
            return "Один миллион";
        }

        StringBuilder string = new StringBuilder("");

        int rank = -1;
        for (int num = number; num != 0; num /= 10) {
            rank++;

            if (rank == 3) {
                if ((num % 100) / 10 != 1) {
                    switch (num % 10) {
                        case 2:
                        case 3:
                        case 4:
                            string.insert(0, "тысячи ");
                            break;
                        case 1:
                            string.insert(0, "тысяча ");
                            break;
                        default:
                            string.insert(0, "тысяч ");
                            break;
                    }
                } else {
                    string.insert(0, "тысяч ");
                }
            }

            string.insert(0, subThousand(rank, num));

            if ((num % 100) / 10 == 1 && num % 10 != 0 && rank % 3 == 0) {
                rank++;
                num /= 10;
            }
        }

        string.setCharAt(0, Character.toUpperCase(string.charAt(0)));
        string.deleteCharAt(string.length()-1);
        return string.toString();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text;
        private final LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.item_list_text);
            layout = itemView.findViewById(R.id.item_list_layout);
        }
    }
}
