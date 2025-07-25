package service;

import java.util.Arrays;
import java.util.List;

import model.SurveyForm;


public class AdSuggestionService {
    public static class SuggestionResult {
        private String mediaId;         // 追加: 一番おすすめの媒体名
        private String suggestion;         // おすすめ媒体
        private String reason;             // なぜおすすめするかの理由・解説
        private List<String> evidenceUrls;        // 根拠となるデータやソースURL

        public SuggestionResult(String mediaId, String suggestion, String reason, List<String> evidenceUrl) {
            this.mediaId = mediaId;
            this.suggestion = suggestion;
            this.reason = reason;
            this.evidenceUrls = evidenceUrl;
        }

        public String getMediaId() { return mediaId; }
        public String getSuggestion() { return suggestion; }
        public String getReason() { return reason; }
        public List<String> getEvidenceUrls() { return evidenceUrls; }
    }

    public SuggestionResult suggest(SurveyForm form) {
        String age = form.getAgeGroup();
        String gender = form.getGender();
        String interest = form.getInterest();

        // より具体的な条件から順番にチェック
        // 1. 年齢 + 性別 + 関心の組み合わせ（最も具体的）
        if ("10代".equals(age) && "エンタメ".equals(interest)) {
            return new SuggestionResult(
                "TikTok広告",
                "TikTok広告やYouTube広告がオススメです。",
                "10代のエンタメコンテンツ消費は短尺動画に集中しており、TikTokやYouTube Shortsが強い影響力を持っています。",
                Arrays.asList("https://www.cyberagent.co.jp/news/detail/id=31459")
            );
        }
        
        if ("20代".equals(age) && "音楽".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告",
                "YouTube広告やInstagram広告が適しています。",
                "音楽コンテンツとの親和性が高く、視覚・聴覚で訴求可能なYouTubeやInstagramは若年層へのアプローチに効果的です。",
                Arrays.asList("https://www.statista.com/statistics/289658/youtube-monthly-mobile-reach-age-gender/")
            );
        }
        
        if ("男性".equals(gender) && "ゲーム".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告",
                "YouTube広告とTwitter広告が効果的です。",
                "ゲーム分野は動画やレビューが重視され、YouTubeでのプレイ動画視聴が一般的です。また、速報性のある情報発信にはTwitterも有効です。",
                Arrays.asList("https://sienca.jp/blog/advertising/youtube-trend/")
            );
        }
        
        if ("女性".equals(gender) && "ファッション".equals(interest)) {
            return new SuggestionResult(
                "Instagram広告",
                "Instagram広告やLINE広告が適しています。",
                "女性のファッション関心層はInstagramやLINEを利用する割合が高く、視覚的な訴求が有効です。",
                Arrays.asList("https://assist-all.co.jp/column/instagram/20250605-4932/?utm_source=chatgpt.com")
            );
        }

        // 2. 年齢 + 関心の組み合わせ
        if ("60代以上".equals(age)) {
            return new SuggestionResult(
                "LINE広告",
                "LINE広告やFacebook広告が効果的です。",
                "60代以上はLINEやFacebookの利用率が高く、スマートフォンでの情報収集が主流となっています。",
                Arrays.asList("https://www.nissinko.com/tips/720/", "https://loycus.jp/line-marketing/line-is-the-best-for-senior-marketing/")
            );
        }
        
        // 3. 関心のみの条件（年齢・性別に関係なく）
        if ("健康・美容".equals(interest)) {
            return new SuggestionResult(
                "Instagram広告",
                "Instagram広告が特に効果的です。",
                "美容系コンテンツはビジュアル訴求が命で、Instagramは写真・動画ベースの広告が非常に有効です。",
                Arrays.asList("https://assist-all.co.jp/column/instagram/20250605-4932/?utm_source=chatgpt.com")
            );
        }

        if ("ビジネス".equals(interest)) {
            return new SuggestionResult(
                "YouTube広告",
                "YouTube広告やLinkedIn広告がオススメです。",
                "ビジネス関心層はYouTubeでビジネス系動画やLinkedInで情報収集する傾向が強いため、両媒体が有効です。",
                Arrays.asList("https://directsourcing-lab.com/blog/advertisement/linkedin-ad-type/?utm_source=chatgpt.com", "https://jp.linkedin.com/company/linkedin-japan")
            );
        }
        
        if ("グルメ".equals(interest)) {
            return new SuggestionResult(
                "Instagram広告",
                "Instagram広告やLINE広告が特におすすめです。",
                "グルメ分野は視覚的な訴求力が重要で、Instagramでの写真・動画展開や、LINEを使ったクーポン・再訪促進が効果的です。",
                Arrays.asList("https://assist-all.co.jp/column/instagram/20250605-4932/?utm_source=chatgpt.com")
            );
        }

        // 4. 年齢のみの条件（関心が一般的な場合）
        if ("10代".equals(age)) {
            return new SuggestionResult(
                "YouTube広告",
                "YouTube広告やInstagram広告が効果的です。",
                "10代は動画コンテンツの消費が多く、YouTubeやInstagramでの広告が高い効果を発揮します。",
                Arrays.asList("https://www.cyberagent.co.jp/news/detail/id=31459")
            );
        }

        // 5. デフォルト（どの条件にも当てはまらない場合）
        return new SuggestionResult(
            "YouTube広告",
            "YouTube広告、LINE広告の組み合わせをおすすめします。",
            "幅広い年齢層にリーチできるYouTubeとLINEの併用は、ターゲットが明確でない場合でも効果が期待できます。",
            Arrays.asList("https://www.dentsu.co.jp/news/release/2023/0517-010669.html")
        );
    }
}