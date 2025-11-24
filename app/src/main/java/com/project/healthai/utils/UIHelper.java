package com.project.healthai.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class UIHelper {

    /**
     * Add a bullet point with colored indicator
     */
    public static void addBulletPoint(Context context, LinearLayout container,
                                      String text, String colorHex) {
        TextView bulletView = new TextView(context);
        bulletView.setText("• " + text);
        bulletView.setTextSize(15);
        bulletView.setTextColor(Color.parseColor("#666666"));
        bulletView.setPadding(
                dpToPx(context, 12),
                dpToPx(context, 8),
                dpToPx(context, 12),
                dpToPx(context, 8)
        );
        bulletView.setLineSpacing(dpToPx(context, 4), 1.0f);
        bulletView.setCompoundDrawablesWithIntrinsicBounds(
                createColoredDot(colorHex), null, null, null
        );
        bulletView.setCompoundDrawablePadding(dpToPx(context, 8));
        container.addView(bulletView);
    }

    /**
     * Add a food item card with details
     */
    public static void addFoodItem(Context context, LinearLayout container,
                                   JSONObject food) throws Exception {
        LinearLayout foodCard = new LinearLayout(context);
        foodCard.setOrientation(LinearLayout.VERTICAL);
        foodCard.setBackgroundColor(Color.parseColor("#F8F9FA"));
        foodCard.setPadding(
                dpToPx(context, 12),
                dpToPx(context, 12),
                dpToPx(context, 12),
                dpToPx(context, 12)
        );

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(context, 12));
        foodCard.setLayoutParams(cardParams);

        // Food name and portion
        TextView nameView = new TextView(context);
        nameView.setText(food.getString("item") + " - " + food.getString("portion"));
        nameView.setTextSize(16);
        nameView.setTextColor(Color.parseColor("#333333"));
        nameView.setTypeface(null, Typeface.BOLD);
        foodCard.addView(nameView);

        // Calories
        TextView caloriesView = new TextView(context);
        caloriesView.setText(food.getInt("estimatedCalories") + " calories");
        caloriesView.setTextSize(14);
        caloriesView.setTextColor(Color.parseColor("#666666"));
        caloriesView.setPadding(0, dpToPx(context, 4), 0, dpToPx(context, 4));
        foodCard.addView(caloriesView);

        // Why included
        TextView whyView = new TextView(context);
        whyView.setText("→ " + food.getString("whyIncluded"));
        whyView.setTextSize(14);
        whyView.setTextColor(Color.parseColor("#666666"));
        whyView.setLineSpacing(dpToPx(context, 4), 1.0f);
        foodCard.addView(whyView);

        container.addView(foodCard);
    }

    /**
     * Populate bullet points from JSON array
     */
    public static void populateBulletList(Context context, LinearLayout container,
                                          JSONArray items, String colorHex) throws Exception {
        container.removeAllViews();
        for (int i = 0; i < items.length(); i++) {
            addBulletPoint(context, container, items.getString(i), colorHex);
        }
    }

    /**
     * Populate food items from JSON array
     */
    public static void populateFoodList(Context context, LinearLayout container,
                                        JSONArray foods) throws Exception {
        container.removeAllViews();
        for (int i = 0; i < foods.length(); i++) {
            addFoodItem(context, container, foods.getJSONObject(i));
        }
    }

    /**
     * Capitalize first letter of string
     */
    public static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    /**
     * Create a colored dot drawable
     */
    private static GradientDrawable createColoredDot(String colorHex) {
        GradientDrawable dot = new GradientDrawable();
        dot.setShape(GradientDrawable.OVAL);
        dot.setColor(Color.parseColor(colorHex));
        dot.setSize(20, 20);
        return dot;
    }

    /**
     * Convert dp to pixels
     */
    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}