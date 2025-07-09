package service;

import java.util.Arrays;
import java.util.List;

import model.SurveyForm;


public class AdSuggestionService {
    public static class SuggestionResult {
        private String suggestion;         // おすすめ媒体
        private String reason;             // なぜおすすめするかの理由・解説
        private List<String> evidenceUrls;        // 根拠となるデータやソースURL

        public SuggestionResult(String suggestion, String reason, List<String> evidenceUrl) {
            this.suggestion = suggestion;
            this.reason = reason;
            this.evidenceUrls = evidenceUrl;
        }

        public String getSuggestion() { return suggestion; }
        public String getReason() { return reason; }
        public List<String> getEvidenceUrls() { return evidenceUrls; }
    }

    public SuggestionResult suggest(SurveyForm form) {
        String age = form.getAgeGroup();
        String gender = form.getGender();
        String interest = form.getInterest();


        if ("10代".equals(age) || "エンタメ".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告やInstagram広告が効果的です。",
                "エンタメコンテンツの消費は短尺動画に集中しており、YouTube ShortsやInstagramが強い影響力を持っています。",
                Arrays.asList("https://tam-tamlo.com/en/319") // YouTube広告資料URL")
            );
        }
        
        if ("20代".equals(age) && "音楽".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告やInstagram広告が適しています。",
                "音楽コンテンツとの親和性が高く、視覚・聴覚で訴求可能なYouTubeやInstagramは若年層へのアプローチに効果的です。",
                Arrays.asList("https://www.statista.com/statistics/289658/youtube-monthly-mobile-reach-age-gender/")
            );
        }
        
        if ("男性".equals(gender) && "ゲーム".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告とTwitter広告が効果的です。",
                "ゲーム分野は動画やレビューが重視され、YouTubeでのプレイ動画視聴が一般的です。また、速報性のある情報発信にはTwitterも有効です。",
                Arrays.asList("https://www.famitsu.com/news/202203/04253214.html")
            );
        }
        
        if ("10代".equals(age) && "エンタメ".equals(interest)) {
            return new SuggestionResult(
                "TikTok広告やYouTube広告がオススメです。",
                "エンタメコンテンツの消費は短尺動画に集中しており、TikTokやYouTube Shortsが強い影響力を持っています。",
                Arrays.asList("https://www.marke-media.net/tiktok-youth/")
            );
        }
        
        if ("健康・美容".equals(interest)) {
            return new SuggestionResult(
                "Instagram広告が特に効果的です。",
                "美容系コンテンツはビジュアル訴求が命で、Instagramは写真・動画ベースの広告が非常に有効です。",
                Arrays.asList("https://www.japanbuzz.info/top-10-japanese-food-cooking-influencers/\n")
            );
        }
        

        if ("女性".equals(gender) && "ファッション".equals(interest)) {
            return new SuggestionResult(
                "Instagram広告やLINE広告が適しています。",
                "女性のファッション関心層はInstagramやLINEを利用する割合が高く、視覚的な訴求が有効です。",
                Arrays.asList("https://www.statista.com/statistics/972939/japan-instagram-user-gender/") // Statista Instagram demographics Japan
            );
        }

        if ("ビジネス".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告やLinkedIn広告がオススメです。",
                "ビジネス関心層はYouTubeでビジネス系動画やLinkedInで情報収集する傾向が強いため、両媒体が有効です。",
                Arrays.asList("https://jp.linkedin.com/company/linkedin-japan") // LinkedIn公式
            );
        }

        if ("60代以上".equals(age)) {
            return new SuggestionResult(
                "LINE広告やFacebook広告が効果的です。",
                "60代以上はLINEやFacebookの利用率が高く、スマートフォンでの情報収集が主流となっています。",
                Arrays.asList("https://www.tokyomarketing.net/full-guide-to-line-digital-marketing-in-japan/" , "https://nextlevel.global/blog/2024/12/19/how-to-use-line-for-marketing-insights-for-engaging-japans-most-connected-audience/") // NRI シニア層のSNS利用
            );
        }
        
        if ("グルメ".equals(interest)) {
            return new SuggestionResult(
                "Instagram広告やLINE広告が特におすすめです。",
                "グルメ分野は視覚的な訴求力が重要で、Instagramでの写真・動画展開や、LINEを使ったクーポン・再訪促進が効果的です。",
                Arrays.asList("https://www.sbpayment.jp/solution/line_ads/column/20220221_001/")
            );
        }

        return new SuggestionResult(
            "YouTube広告、LINE広告の組み合わせをおすすめします。",
            "幅広い年齢層にリーチできるYouTubeとLINEの併用は、ターゲットが明確でない場合でも効果が期待できます。",
            Arrays.asList("https://www.dentsu.co.jp/news/release/2023/0517-010669.html") // 電通メディアイノベーションラボ SNSユーザー調査
        );
    }
}