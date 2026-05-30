package com.mindspace.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mindspace.app.data.model.Quote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteFetcher {
    private static final String PREFS_NAME = "QuotePrefs";
    private static final String KEY_QUOTE_CONTENT = "quoteContent";
    private static final String KEY_QUOTE_AUTHOR = "quoteAuthor";
    private static final String KEY_QUOTE_DATE = "quoteDate";

    private static final List<Quote> LOCAL_QUOTES = new ArrayList<>();

    static {
        LOCAL_QUOTES.add(createQuote("生活不是等待风暴过去，而是学会在雨中跳舞。", "匿名"));
        LOCAL_QUOTES.add(createQuote("每一天都是新的开始，珍惜当下，拥抱未来。", "匿名"));
        LOCAL_QUOTES.add(createQuote("成功不是终点，失败也不是终结，唯有勇气才是永恒。", "温斯顿·丘吉尔"));
        LOCAL_QUOTES.add(createQuote("你的心态决定你的高度，你的努力决定你的未来。", "匿名"));
        LOCAL_QUOTES.add(createQuote("不要等待机会，而要创造机会。", "萧伯纳"));
        LOCAL_QUOTES.add(createQuote("人生最大的荣耀不在于从不跌倒，而在于每次跌倒后都能爬起来。", "纳尔逊·曼德拉"));
        LOCAL_QUOTES.add(createQuote("梦想不会逃跑，逃跑的永远是自己。", "稻盛和夫"));
        LOCAL_QUOTES.add(createQuote("每一个不曾起舞的日子，都是对生命的辜负。", "尼采"));
        LOCAL_QUOTES.add(createQuote("把每一天都当作生命的最后一天去过，你会发现生活更加美好。", "史蒂夫·乔布斯"));
        LOCAL_QUOTES.add(createQuote("真正的勇气是在看清生活的真相后，依然热爱生活。", "罗曼·罗兰"));
        LOCAL_QUOTES.add(createQuote("只有不断寻找机会的人，才会及时把握机会。", "匿名"));
        LOCAL_QUOTES.add(createQuote("不要为小事遮住视线，我们还有更大的世界。", "匿名"));
    }

    private static Quote createQuote(String content, String author) {
        Quote quote = new Quote();
        quote.setContent(content);
        quote.setAuthor(author);
        return quote;
    }

    public interface QuoteListener {
        void onQuoteLoaded(Quote quote);
        void onQuoteError(String error);
    }

    public static void getDailyQuote(Context context, QuoteListener listener) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedDate = prefs.getString(KEY_QUOTE_DATE, "");
        String today = DateUtils.getTodayString();

        if (today.equals(savedDate)) {
            String content = prefs.getString(KEY_QUOTE_CONTENT, "");
            String author = prefs.getString(KEY_QUOTE_AUTHOR, "");
            if (!content.isEmpty()) {
                Quote quote = new Quote();
                quote.setContent(content);
                quote.setAuthor(author);
                listener.onQuoteLoaded(quote);
                return;
            }
        }

        fetchNewQuote(context, listener);
    }

    public static void fetchNewQuote(Context context, QuoteListener listener) {
        try {
            Quote quote = getRandomLocalQuote();
            saveQuote(context, quote);
            listener.onQuoteLoaded(quote);
        } catch (Exception e) {
            listener.onQuoteError("获取名言失败: " + e.getMessage());
        }
    }

    private static Quote getRandomLocalQuote() {
        Random random = new Random();
        int index = random.nextInt(LOCAL_QUOTES.size());
        return LOCAL_QUOTES.get(index);
    }

    private static void saveQuote(Context context, Quote quote) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_QUOTE_CONTENT, quote.getContent());
        editor.putString(KEY_QUOTE_AUTHOR, quote.getAuthor());
        editor.putString(KEY_QUOTE_DATE, DateUtils.getTodayString());
        editor.apply();
    }

    public static Quote getSavedQuote(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String content = prefs.getString(KEY_QUOTE_CONTENT, "");
        String author = prefs.getString(KEY_QUOTE_AUTHOR, "");
        if (!content.isEmpty()) {
            Quote quote = new Quote();
            quote.setContent(content);
            quote.setAuthor(author);
            return quote;
        }
        return getDefaultQuote();
    }

    private static Quote getDefaultQuote() {
        return LOCAL_QUOTES.get(0);
    }
}
