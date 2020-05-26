package com.classyinc.classytreasurer;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.Random;

public class RewardActivity extends AppCompatActivity  implements RewardedVideoAdListener {


    private RewardedVideoAd mRewardedVideoAd;
    private TextView textReward;

    Button btn;

    private String[] tips = {"Always inculcate saving habits in your children:\n" +
            "\n" +
            "Nowadays kids easily ask for money for petty purchases from their parents. This is because they don’t know how much efforts go in earning that Rs.100. Always inculcate saving habits from an early age. Make children aware about the efforts needed to earn. This will reduce spending pressure on us as most of the spending happens due to children’s emotional pressure.",
            "Get Paid What You're Worth and Spend Less Than You Earn",
            "Unfortunately, a class called \"finance for young adults\" isn't usually part of a high school curriculum.",
            "Taking the time to learn a few critical financial rules may help you build a healthy financial future.",
    "if you buy things that you dont need then you will sell things that you actually need",
    "Start an emergency fund and pay into it every month, even if it is a small amount.",
            "Understanding how money works is the first step toward making your money work for you. ",
            "Pay YourSelf First!"
    ,"You need motivation to start adopting better money habits, and if you craft a vision board, it can help remind you to stay on track with your financial goals.",
    "Spend Less Than You Earn",
    "A popular and effective way to budget is with the 50/30/20 rule. How it works is 50% of your income goes towards the necessities (bills, food, housing, etc.), 20% of your income goes towards savings and the remaining 30% you can use for whatever you please.",
    "A Credit Card is Not Free Money",
    "So, what is bad debt?\n" +
            "Bad debt is any debt that's acquired through purchasing something that's going to lose value and generate zero revenue.",
    "If you lost your job tomorrow would you have enough money to live off while you look for a new one? ",
    " Know Your Net Worth\n" +
            "Net worth can seem like a tricky topic, but it's quite simple. Your net worth is how much money you are worth. If you were to sell everything you own, then pay off everything you owe, how much money would be left?\n" +
            "\n" +
            "That's your net worth.",
    "That's your net worth.\n" +
            "\n" +
            "Here's what that looks like in equation form:\n" +
            "\n" +
            "Net worth = Assets (what you own) – Liabilities (what you owe)",
    "If you have a positive net worth that's good. Continue working to increase your net worth even more.",
    "If you have a negative net worth, you need to take a look at your budget and come up with a plan to increase your net worth. If you're young and you have a big student loan, you shouldn't worry too much as you haven't even started working yet.",
    "Money in a savings account loses value over time.",
    "Take action. Start working on improving your finances today, not tomorrow.",
    "F***! Credit Card!",
    "Don’t Loan Money You Can’t Afford to Lose",
    "If your car isn’t worth much, just get liability coverage (unless full coverage is legally required where you live).",
     "50% of your income goes to essentials and necessities:\n" +
             "\n" +
             "Food\n" +
             "Rent/mortgage\n" +
             "Utilities\n" +
             "Gas or transportation",
    "30% of your income goes to lifestyle choices:\n" +
            "\n" +
            "Monthly subscriptions\n" +
            "Ordering out\n" +
            "Entertainment\n" +
            "Vacations",
    "Don’t Be Afraid to Negotiate Prices",
    "Find Cheap/Free Ways to Have Fun",
    "Pay Your Bills Before Making Lifestyle Purchases",
    "Get a Roommate\n" +
            "Want to know how to cut your household expenses in half? Get a roommate. If you feel comfortable living with someone else, getting a roommate can be an amazing way to save a lot of money.",
   "Don’t Fall for The Social Media Lifestyle Hoax",
    "Start Investing Early, and Often",
    "Learn About Compounding Interest",
    "Keep F**K You Money, Just in Case","The important thing to remember is that you should save something. Even if it’s just $20 a paycheck, it’s a start. Don’t stretch yourself too thin trying to follow advice that doesn’t apply to you.",
    "Taking control of your own finances means you’re able to stand on your own two feet without relying on a single source of income, your family or anyone else.\n" +
            "\n",
    "Get Paid What You're Worth and Spend Less Than You Earn. It may sound simple, but many people struggle with this first rule.",
    "Don’t let the term “minimum payment” confuse you. When it comes to paying your credit card, settling for the bare minimum makes you a slave to interest.",
    "estimate your daily interest:\n" +
            "\n" +
            "(interest rate) x (current principal balance) ÷ (number of days in the year) = daily interest\n" +
            "\n",
    "Sometimes spending can turn into a mindless habit. The more you do it, the easier it becomes.",
    "Get on top of your finances by signing up for text or email alerts for your credit cards, student loans, and other financial accounts.",
    "Take one part of your budget and pay only in cash",
    "The temptation to spend is everywhere, but the hottest spot has to be your inbox. Consider unsubscribing from sales emails using Unroll.me.",
    "Sell unwanted goods","Changing your financial life can be tough, but don’t let the big picture get you down.",
    "How much tax do u pay?","Buy an outfit you’ll only wear once: Certain occasions will call for ensembles that you can only wear one time. Try to avoid these occasions if you’re not in a good place, financially. Renting (or borrowing) an outfit may be cheaper. Worst case scenario, buy it, make sure you keep it in excellent condition, and sell it immediately afterward, on Craigslist, for example.",
    " Use your credit card only in emergencies:\n" +
            "\n" +
            "Avoid credit card calls on your mobile with plenty of benefits. Having more than one credit card may lead into debt trap. Yes, we should have a credit card but only to use in emergencies. In normal course use debit card only.",
    "Always secure family by buying adequate term life insurance cover",
    "Make family aware about your investments and assets:\n" +
            "\n" +
            "Always discuss finances with spouse and kids. Never keep them in the dark about the financial assets which have been built for their better future",
    "Of all those riches, around 70% are Self-made.\n" +
            "\n" +
            "Yes, seventy percent billionaires are self-made vs thirty percent inherited.\n" +
            "\n" +
            "So, what do they do that makes them different?\n" +
            "\n" +
            "They invest.\n" +
            "\n" +
            "Now, this gets tricky here, they invest not only in terms of money.",
    "Learn skills that will double your income. That will increase your income streams. Acquiring skills is the best investment of money and time.",
    "Be patient, because Compounding interest works in all aspects of life. Knowledge, Skills & of course Finances. It is Gold.",
    "Once you understand the math behind investing in yourselves, you will give it the highest priority. Use your money to grow your selves better than yesterday.",
    "Never buy a car if you cannot afford it(Fuel, Maintenance). Never buy a car if you do not have a regular use of it. ",
    " Do not buy a car just for the sake of social status.",
    "Never spend lavishly on functions like marriage. Instead gift your children with a F.D or gold which will be useful for them in future.",
    "Never compete with your relatives, friends or neighbours while spending.",
    "Make your children financially literate not just academically.",
    "As far as possible avoid EMI culture.",
    "Don’t put your kids in expensive schools.. put them in fairly good reasonable schools. Kids learn more from within their house than in the school.",
    "Keep an extra packet of condoms handy in your closet to avoid beautiful accidents.",
    "Don't order starters at restaurants, eat a heavy meal instead, upto 50% savings.",
    "Even if you earn too less and struggle to meet ends, never forget to keep a very petty amount as saving every month and invest it. (Start from INR 500 if possible, but start)",
    "Keep handy jute or cloth bag in your vehicle to avoid paying 10 rupees for a 1 rupee bag every time.",
    "Get a piggy bank for your baby and start putting coins and loose currency into it. Open it every six months and put that money in an investment for a financially secured future of your child.",
    "Isn’t it ridiculous to spend 400 rupees for a bucket full large popcorn + 2 cokes of 150 ml?\n" +
            "\n" +
            "Try to avoid it when you go to movies!\n" +
            "\n" +
            "Do you know why?\n" +
            "\n" +
            "For 400 rupees we can have a stomach full of tasty food with family members at home.\n" +
            "\n" +
            "Let me break down it for you.\n" +
            "\n" +
            "1.5 Kg chicken = 200 rupees\n" +
            "1/2 kg Basmathi rice (to mix with normal rice) = 45 rupees\n" +
            "Chicken Biriyani Masala Packet = 10 rupees\n" +
            "Chicken Fry Masala Packet = 10 rupees\n" +
            "Garam masala packet = 10 rupees\n" +
            "Capsicum = 10 rupees\n" +
            "Ginger garlic paste = 10 rupees\n" +
            "Tomatoes = 10 rupees\n" +
            "Onion = 10 rupees\n" +
            "Green chilly = 5 rupees\n" +
            "Lemon (3 no.) = 10 rupees\n" +
            "Peas packet = 10 rupees\n" +
            "Egg (6 no.) = 30 rupees\n" +
            "Curd (1/2 ltr) = 20 rupees\n" +
            "Coriander leaves & Mint leaves = 10 rupees",
    "Don't spend of liabilities like phones, clothes, phones, Footwear, TV etc..",
    "Don’t buy a car/ bike on EMI (unless not owning it seriously hinders your day to day life). If you can’t afford it, consider buying a used vehicle.",
    "Don’t rush to “invest” in a house immediately. It is a huge financial (and often emotional) commitment & may tie you down.",
    "Insurance is not investment. It is a risk management tool. Don’t mix investment and insurance",
    "Invest. It’s OK to start small. But do start investing. Increase as your income grows.",
    "Avoid lifestyle trap. Running after the latest iPhone or bike or car or gadgets may not be worth it, especially if you can’t afford it.",
    "Avoid things which may be harmful for health – Smoking, alcohol, drugs may not just potentially ruin your health, but also come at a huge financial (and sometimes legal!) cost too.",
    "Understand inflation. And lifestyle inflation. And try to mitigate lifestyle inflation (avoiding may not always be possible!). Inflation needs to factor in your financial plans, especially over a long time horizon.",
    "Fix the saving percentage you want to save. When you are single try to save the maximum up to 50%.",
    "Save before you spend. Create a separate account where you can transfer stipulated saving amount on the first day of the month from your salary account.",
    "Never get into the debt trap. Please never take any expensive loans to fulfill your aspiration. The debt will hamper your saving.",
    "Wealth creation is a long term phenomenon. Stick to your asset allocation for the long term. Don't get panic about the short term volatility in equity markets.",
    "Get rich slow. News flash. You don’t need to be a millionaire by 35. I mean, what are you going to do then? And trying to do that you’ll make a lot of unforced errors. Then you’ll remember 1 and 2.",
    "Forget about it. The markets go up. They go down. Sometimes they scare the hell out of you. If you’re broadly diversified then add to your investments. Every month. Why not buy while everything is on sale?",
    "Google your map. How much will you need? When do you need it? How much will you need to save? How much can you expect to make per year? Ok, no fantasies. ",
    "Have fun. Yeah, I’m a little weird here. I think the game of building wealth is fun. Learning, strategizing, investing and then seeing your numbers grow is fun. Sure as hell beats the opposite.",
    "Spend less than you make.\n" +
            "Rule #1 now and forever. If you do this, you will never go broke.\n" +
            "This means creating and sticking with a budget.",
    "There are only two reasons to borrow money:\n" +
            "To purchase an appreciating asset (such as a business or real estate)\n" +
            "To fund education",
    "There are two smart investments:\n" +
            "Investing in yourself\n" +
            "This can be through a side project you hope to turn into a business (like real estate investing for example)\n" +
            "This could also be through further education (which we would hope would increase your earnings potential)\n" +
            "Investing across the broad global stock market through diversified low-cost ETF or mutual funds",
    "Have a goal in mind.\n" +
            "Decide how much money you want to have to live your version of a “happy” (not perfect) life.",
    "Personal finance is pretty straightforward and simple when you cut it down to the bone:\n" +
            "\n" +
            "More money coming in than going out\n" +
            "Use any extra money to make more money\n" +
            "Repeat until you have the desired amount of money",
    "Do exercise and read non-fictional books on a regular basis which can maintain your life in any situation.",
    "Do n’t party too much otherwise, you'll make really bad decisions about your life and probably you may regret later.",
    "Do n’t start EMIs unnecessarily, otherwise, you have to pay a lot of interest in the long term.",
    "Do n’t buy too many clothes and other unnecessary kinds of stuff, try to live a simple meaningful life. Remember your choices will lead you to become wealthy in your 30s or 40s."};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        MobileAds.initialize(RewardActivity.this,"ca-app-pub-6826247666109501~7454811121");

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();


        textReward = findViewById(R.id.txt_reward_id);
        btn=findViewById(R.id.btn_reward_id);

        Toast.makeText(this, "Your Tip will take time to load!", Toast.LENGTH_SHORT).show();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
    }

    private void loadRewardedVideoAd() {

        mRewardedVideoAd.loadAd("ca-app-pub-6826247666109501/4445504400",new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {


        if(mRewardedVideoAd.isLoaded()) {

            mRewardedVideoAd.show();
        }

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

        Toast.makeText(this, "Your Tip Will Be Displayed After the Ad Ends!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {

        Toast.makeText(this, "Ad Closed...!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        Random random = new Random();
        int num = random.nextInt(tips.length);
        textReward.setText(tips[num]);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        if (errorCode == AdRequest.ERROR_CODE_INTERNAL_ERROR) {
            Toast.makeText(RewardActivity.this, "Internal error!", Toast.LENGTH_SHORT).show();
        }
        else if(errorCode == AdRequest.ERROR_CODE_INVALID_REQUEST) {
            Toast.makeText(RewardActivity.this, "Invalid Ad Request!", Toast.LENGTH_SHORT).show();
        }
        else if(errorCode == AdRequest.ERROR_CODE_NETWORK_ERROR){
            Toast.makeText(RewardActivity.this,"Please Check your Internet Connection!", Toast.LENGTH_SHORT).show();
        }
        else if(errorCode == AdRequest.ERROR_CODE_NO_FILL){
            Toast.makeText(RewardActivity.this,"Lack of Ads in Inventory!", Toast.LENGTH_SHORT).show();
        }
        else if(errorCode == AdRequest.ERROR_CODE_APP_ID_MISSING){
            Toast.makeText(RewardActivity.this, "App Id Missing!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(getApplicationContext(), "Your Tip is Here!", Toast.LENGTH_SHORT).show();
    }

}