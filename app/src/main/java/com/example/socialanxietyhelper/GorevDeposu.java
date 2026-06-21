package com.example.socialanxietyhelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GorevDeposu — 6 kategori × 8 bölüm × 15 görev = 720 görev
 *
 * Kategori indeksleri (WorldsActivity ile aynı sıra):
 *   0 = Genel
 *   1 = İş
 *   2 = Okul
 *   3 = Mahalle
 *   4 = Romantik
 *   5 = Aile
 *
 * Kullanım:
 *   GorevDeposu.getGorevler(categoryIdx, worldNum)  → List<Gorev>
 */
public class GorevDeposu {

    private static final Map<String, List<Gorev>> data = new HashMap<>();

    private static String key(int cat, int world) {
        return cat + "_" + world;
    }

    private static void add(int cat, int world, String baslik, String aciklama, int zorluk) {
        String k = key(cat, world);
        if (!data.containsKey(k)) data.put(k, new ArrayList<>());
        boolean kilitli = !(cat == 0 && world == 1);
        data.get(k).add(new Gorev(baslik, aciklama, zorluk, kilitli));
    }

    public static List<Gorev> getGorevler(int categoryIdx, int worldNum) {
        List<Gorev> list = data.get(key(categoryIdx, worldNum));
        return list != null ? list : new ArrayList<>();
    }

    /** Eski LevelMapActivity çağrısıyla uyumluluk */
    public static List<Gorev> getSeviyeGorevleri(int level) {
        return getGorevler(0, level);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KATEGORİ 0 — GENEL
    // ══════════════════════════════════════════════════════════════════════

    static { // Bölüm 1 — Zemin
        add(0,1,"Tarihi Söyle","Aynaya bak ve kendine bugünün tarihini söyle — sesli, net.",1);
        add(0,1,"Parfüm Sık","Dışarı çıkmadan parfüm sık. Sadece çünkü istedin.",1);
        add(0,1,"Balkonda Nefes","Sabah kalktığında balkona çık ve 3 derin nefes al.",1);
        add(0,1,"Karşıya Bak","Eve karşıya bakarak yürü, 5 dakika.",1);
        add(0,1,"Bitki Al","Kendine bir bitki al ve onu yetiştirmeyi öğren.",1);
        add(0,1,"Bağcık Bağla","Dışarı çıkınca apartmanın önünde bağcıklarını bağla. Aceleden değil.",2);
        add(0,1,"Yukarı Bak","Dışarıda yürürken başını kaldır ve binaların üst katlarına bak.",2);
        add(0,1,"Ahtapot Paragrafı","Ahtapotlarla ilgili bir paragraf bul, aynada kendine bakarak oku.",2);
        add(0,1,"Bilinmez Sokak","Bilmediğin bir sokakta 10 dakika yürü.",2);
        add(0,1,"Fiş Oku","Markette rastgele bir ürün al, kasanın yanında fişini dikkatlice oku, çöpe at.",2);
        add(0,1,"Pencereden Bak","Otobüste ya da metroda oturup pencereden dışarıyı izle.",2);
        add(0,1,"Kitabı Aç","Bir kitapçıya gir, rastgele bir kitabı aç ve ilk sayfayı oku.",2);
        add(0,1,"Selam Ver","Bugün birine günaydın ya da kolay gelsin de.",3);
        add(0,1,"Parkta Otur","Bir parkta otur ve etrafındaki insanları 5 dakika gözlemle.",3);
        add(0,1,"Ters Yön","Dışarıda yürürken telefonda konuşuyormuş gibi yap, 'tamam geliyorum' de ve tam ters yöne dönerek yürü.",3);
    }

    static { // Bölüm 2 — Varlık
        add(0,2,"Ürünü Oku","Markette bir ürünün içeriğini 2-3 dakika okuyun, almadan rafın önünde durun.",1);
        add(0,2,"Parkta Otur","Parkta tek başınıza bir bankta oturun, telefonsuz, 10 dakika.",1);
        add(0,2,"Mağazada Dene","Bir mağazada bir şeyi deneyin, almayın, teşekkür edip çıkın.",1);
        add(0,2,"Fiyat Sor, Alma","Esnaftan bir ürünün fiyatını sorun, düşüneceğim deyip ayrılın.",1);
        add(0,2,"Tek Başına Kafe","Bir kafeye tek başınıza girin, oturun, bir şeyler için, acele etmeyin.",1);
        add(0,2,"Yavaş Yürü","Kalabalık bir caddede acele etmeden, yavaşça yürüyün.",2);
        add(0,2,"Tek Başına Ye","Bir lokanta veya kafede tek başınıza oturup yemek yiyin.",2);
        add(0,2,"Kuyrukta Bekle","Uzun bir kuyruğa bilerek girin, sabırla bekleyin, acele etmeyin.",2);
        add(0,2,"Kitapçıda Kal","Kitapçıya girin, satın almadan kitaplara bakın, en az 15 dakika kalın.",2);
        add(0,2,"Tek Başına Sinema","Bir filme tek başınıza gidin.",2);
        add(0,2,"Bankta Otur","Kalabalık bir mekânda boş bir bankı bulun, oturun, etrafı izleyin.",2);
        add(0,2,"Yanlış Durak","Bir durak erken inin, yürüyerek gidin, aceleniz yokmuş gibi davranın.",2);
        add(0,2,"AVM'de Dolaş","Alışveriş merkezinde hiçbir şey almadan, amaçsızca 20 dakika dolaşın.",2);
        add(0,2,"Menüyü İncele","Bir restorana girin, menüyü inceleyin, sipariş vermeden çıkın.",2);
        add(0,2,"Pazar Gezisi","Açık hava pazarında hiçbir şey almadan tezgahları gezerek 20 dakika geçirin.",3);
    }

    static { // Bölüm 3 — İlk Adım
        add(0,3,"Asansör Selamı","Asansörde karşılaştığın kişiye gülümseyerek 'günaydın' de.",1);
        add(0,3,"Kasiyer Teşekkürü","Kasiyere 'teşekkürler' de ve 1 saniye göz teması kur.",1);
        add(0,3,"Yol Açma","Birinin önünden geçerken net bir sesle 'pardon' de.",1);
        add(0,3,"Kapı Teşekkürü","Kapıyı senin için tutan birine 'sağ olun' de.",1);
        add(0,3,"İçerik Sorgusu","Kafede sipariş verirken 'Bunun içinde ne var?' diye sor.",1);
        add(0,3,"Sıra Önceliği","Sırada beklerken arkandaki az eşyalı kişiye 'Siz buyurun' de.",2);
        add(0,3,"Adres Sorma","Bildiğin bir yeri bile olsa, yolda birine '... nerede?' diye sor.",2);
        add(0,3,"Esnaf Selamı","Girdiğin bir dükkanda esnafa 'Kolay gelsin' diyerek gir.",2);
        add(0,3,"Pazarlık Denemesi","Esnaftan sembolik bir indirim iste (Örn: 'Düz yapabilir miyiz?').",2);
        add(0,3,"Paket İsteği","Küçük bir şey alsan bile 'Hediye paketi yapabilir misiniz?' de.",2);
        add(0,3,"Renk Sorgusu","Dükkanda 'Bunun başka rengi var mı?' de, yoksa teşekkür et ve çık.",2);
        add(0,3,"Saat Sorma","Yolda birine 'Saatiniz kaç acaba?' diye sor.",3); // Basit ama etkili başlangıç
        add(0,3,"Kısa Muhabbet","Hava hakkında bir yorum yap (Örn: 'Bugün de çok sıcak değil mi?').",3); // Small talk başlangıcı
        add(0,3,"Geri Bildirim","Bir kafede ödeme yaparken 'Kahveniz çok güzeldi' de.",3); // Pozitif onaylama
        add(0,3,"Yer Verme","Toplu taşımada ihtiyacı olan birine yer ver veya yer teklif et.",3); // Sosyal özgüven
    }

    static { // Bölüm 4 — Ses
        add(0,4,"Sesli Günaydın","Apartman görevlisine veya güvenliğe net bir sesle 'Günaydın' de.",1);
        add(0,4,"Telefonda Mekan Sor","Bir kafeyi ara ve 'Kaça kadar açıksınız?' diye sorup teşekkür ederek kapat.",1);
        add(0,4,"Markette Yer Sor","Markette çalışan birine 'Süt nerede?' gibi en basit şeyi sor.",1);
        add(0,4,"Durakta Soru","Otobüs beklerken yanındakine 'Bu hat buradan geçiyor mu?' diye sor.",1);
        add(0,4,"Normal Sesle Konuş","Toplu taşımada biriyle (veya telefonda) fısıldamadan, normal bir tonda konuş.",1);
        add(0,4,"Reddedilme Avı - 1","Kafede menüde asla olmayacak bir şey sor (Örn: 'Taze sıkılmış ejder meyvesi suyu var mı?').",3);
        add(0,4,"Kısa Memnuniyet","Yemek yediğin yerde hesabı öderken 'Elinize sağlık, çok güzeldi' de.",2);
        add(0,4,"Gürültülü Yerde Seslen","Müzik çalan bir kafede garsonu 'Bakar mısınız?' diyerek çağır.",2);
        add(0,4,"Şarkı Mırıldan","Kulaklıkla müzik dinlerken, dışarıda yürürken bir nakaratı alçak sesle mırıldan.",2);
        add(0,4,"Meydanda Konuşma","Kalabalık bir meydanda dur ve telefonla (gerçek veya değil) canlı bir şekilde 2 dakika konuş.",2);
        add(0,4,"Yankı Görevi","Mağazada gezerken gördüğün bir şeyi kendi kendine ama duyulur şekilde söyle (Örn: 'Bu renk güzelmiş').",2);
        add(0,4,"Soru Sor","Bir mağazada ürünün özelliklerini sor (Örn: 'Bu kumaş terletir mi?').",2);
        add(0,4,"Yüksek Sesle Onay","Bir arkadaşına veya tanıdığına uzaktan el sallayıp ismiyle seslen.",3);
        add(0,4,"Reddedilme Avı - 2","Markette sıradayken önündeki kişiye 'Acaba sıranızı bana verebilir misiniz?' diye sor.",3);
        add(0,4,"Düşünceni Belirt","Bir dükkanda 'Bu model pek bana göre değilmiş, teşekkürler' diyerek fikrini sesli söyle.",3);
    }

    static { // Bölüm 5 — Temas
        // Seviye 1: Görünmezliği Kırma (SUDS: 40-50)
        add(0,5,"Raf Komşusu","Markette birinin ürün seçtiği rafa git. Hemen yanındaki ürünü alıp o gidene kadar incele.",1);
        add(0,5,"Göz Teması ve Kafa Selamı","Yolda karşıdan gelen sima olarak tanıdık biriyle 1 saniye göz teması kur ve hafifçe başınla selam ver.(Örn: Komşu)",1);
        add(0,5,"Tezgah Paylaşımı","Markette ekmek veya manav reyonunda birisi ürün seçerken, tam yanına gelip sen de bir şeyler seç.",1);
        add(0,5,"Aynı Masada Otur","Kafede veya kütüphanede, birinin oturduğu masanın en yakınındaki boş masayı seç.",1);
        add(0,5,"Alan Açma","Dışarıda duran birine 'Pardon, şuradan geçebilir miyim?' diyerek onun sana yol açmasını sağla.",3);
        add(0,5,"Burası Boş mu?","Parkta veya kafede birinin yanındaki boş sandalye için 'Pardon, burası boş mu?' diye sor ve otur.",2);
        add(0,5,"Hafif Dokunuş (Eşyalı)","Kalabalıkta birinin yanından geçerken çantan veya montun hafifçe ona değsin; hemen ardından 'Pardon' diyerek göz teması kur.",3);
        add(0,5,"Eşya Aracılığıyla Temas","Para üstünü veya fişi alırken elden ele teslim al, masaya bırakılmasını bekleme.",2);
        add(0,5,"Bank Arkadaşı","Üzerinde sadece bir kişinin oturduğu uzun bir bankın en uzak köşesine değil, tam orta kısmına otur.",1);
        add(0,5,"Kapı Geçişi","Kapıdan geçerken karşıdan gelene yol ver, göz teması kurup 'Buyurun' de.",2);
        add(0,5,"Nesne Ödünç Al","Kütüphanede veya kafede yanındakine 'Pardon, şu masayı biraz kaydırabilir miyiz?' veya 'Priz çalışıyor mu?' diye sor.",3);
        add(0,5,"Görünmez İp","Asansöre birisi bindiğinde merkeze yakın durmaya devam et, o senin yanından geçmek zorunda kalsın.",3);
        add(0,5,"Ürün Sorma","Birinin elinde tuttuğu ürünü işaret ederek 'Pardon, o taze mi/güzel mi?' diye sor.",3);
        add(0,5,"Mağaza Asistanı","Bir mağazada reyon görevlisinin yanına git, elindeki ürünü gösterip 'Bunun dokusu nasıl sizce?' diye sor (fiziksel bir özellik üzerinden).",3);
        add(0,5,"Kısa Yardım İsteme","Birinden 'Pardon, şunu 2 saniye tutabilir misiniz?' de (Örn: Ceketini giyerken çantanı tuttur).",3);
    }

    static { // Bölüm 6 — Bağ
        add(0,6,"Ortak Gözlem","Etrafınızda herkesin gördüğü bir şeyi yorumlayarak yanınızdakiyle sohbet açın.",1);
        add(0,6,"Hava Yorumu","Yanınızdaki biriyle 'Bugün hava çok güzel değil mi?' diye başlayın.",1);
        add(0,6,"Kahve Sırası","Kafe sırasında yanınızdakine 'Burası iyi mi?' diye sorun.",1);
        add(0,6,"Kıyafet Yorumu","Birinin kıyafetini ya da aksesuarını içtenlikle takdir edin.",2);
        add(0,6,"Kitap / Müzik","Birisinin elindeki kitabı ya da kulaklığını görüp 'O nasıl?' diye sorun.",2);
        add(0,6,"Çocuk / Hayvan","Evcil hayvan sahibiyle ya da çocuğunu gezdiren biriyle sohbet başlatın.",2);
        add(0,6,"Durum Yorumu","Yavaş akan bir kuyruğu ya da trafiği yorumlayarak konuşma başlatın.",2);
        add(0,6,"Öneri Ver","Gittiğiniz yerde birine bir öneri yapın — yemek, yer, ürün.",2);
        add(0,6,"Espri Dene","Hafif, konuya uygun bir espri ya da gülümseten bir yorum yapın.",3);
        add(0,6,"İki Cümle","Yabancıyla en az 2 cümle karşılıklı konuşun.",3);
        add(0,6,"Beş Cümle","Başlattığınız konuşmayı en az 5 cümleye taşıyın.",3);
        add(0,6,"İsim Öğren","Sohbet ettiğiniz birinin adını öğrenin ve kendinizi tanıtın.",3);
        add(0,6,"Ortak Nokta","Konuşurken karşıdakiyle ortak bir şey keşfedin ve paylaşın.",3);
        add(0,6,"Gerçek Soru","'Nasılsın?' yerine gerçek bir ilgi gösteren soru sorun.",3);
        add(0,6,"Doğal Veda","Konuşmayı zorlamadan doğal biçimde bitirin: 'İyi günler' ya da 'Görüşürüz.'",2);
    }

    static { // Bölüm 7 — Hat
        add(0,7,"Tekrar Selam","Dün selam verdiğin esnaf ya da kasiyere bugün tekrar selam ver.",1);
        add(0,7,"İsimle Hitap","Adını bildiğin birine — barista, güvenlik, komşu — ismiyle hitap et.",1);
        add(0,7,"Uzat","Normalde 10 saniyede biten bir etkileşimi biraz daha uzat — bir yorum ekle.",2);
        add(0,7,"Esnaf Sohbeti","Alışveriş yaparken esnafla sadece fiyat değil, kısa bir sohbet yap.",2);
        add(0,7,"Geri Dön","Daha önce gittiğin bir kafe ya da dükkana tekrar git, tanıdık gibi davran.",2);
        add(0,7,"Hatırla","Son görüşmenden bir şeyi hatırlayıp karşı tarafa belirt — 'Geçen söylediğiniz...'",2);
        add(0,7,"Tanıdık Ol","Aynı mekâna üç gün üst üste git, her gün biriyle kısa temas kur.",2);
        add(0,7,"Takip Et","Birine verdiğin sözü ya da söylediğin şeyi takip et — 'O kitabı okudunuz mu?'",3);
        add(0,7,"Bağlantıyı Koru","Konuştuğun biriyle iletişim bilgisi paylaş ya da sosyal medyada bağlan.",3);
        add(0,7,"Tekrar Buluş","Kısa sohbet ettiğin biriyle tekrar karşılaşınca sohbeti sen başlat.",3);
        add(0,7,"Derinleş","Yüzeysel bir sohbeti daha kişisel bir konuya taşı.",3);
        add(0,7,"Ara","Uzun süredir görmediğin biriyle iletişime geç — mesaj, arama ya da ziyaret.",3);
        add(0,7,"İki Farklı Kişi","Aynı gün iki farklı tanıdıkla sohbeti sen başlat ve sürdür.",3);
        add(0,7,"Minnet Bildir","Sana yardımı dokunan birine bunu söyle — 'Geçen yardımınız çok işe yaradı.'",3);
        add(0,7,"Süreklilik","Bir haftada aynı kişiyle üç kez etkileşim kur — her seferinde biraz daha derin.",3);
    }

    static { // Bölüm 8 — Alan
        add(0,8,"Gruba Katıl","Bir grup sohbetine sessizce katıl, dinle, varlığını hissettir.",1);
        add(0,8,"Kısa Yorum","Grup sohbetinde tek cümlelik bir yorum yap.",2);
        add(0,8,"Soruya Cevap","Gruba yöneltilen bir soruya cevap ver.",2);
        add(0,8,"Katılıyorum","Birinin fikrine katıldığını açıkça belirt.",2);
        add(0,8,"Fikir Ekle","Grup tartışmasında kendi fikrini ekle.",2);
        add(0,8,"Soru Sor","Gruba bir soru yönelt.",3);
        add(0,8,"Konuya Gir","Herkes konuşurken sen de dahil ol.",3);
        add(0,8,"Farklı Görüş","Saygılı biçimde farklı bir görüş bildir.",3);
        add(0,8,"Espri Yap","Grupta hafif bir espri ya da anekdot paylaş.",3);
        add(0,8,"Yeni Konu","Bitmiş bir sohbette yeni bir konu sen aç.",3);
        add(0,8,"İki Katkı","Grupta birbirini izleyen iki katkıda bulun.",3);
        add(0,8,"Özet Yap","Grup tartışmasını sen özetle ya da toparlat.",3);
        add(0,8,"Teşekkür Et","Birinin katkısını grup içinde takdir et.",3);
        add(0,8,"Lider Ol","Bir konuyu sen aç ve grubu o konu etrafında topla.",3);
        add(0,8,"Moderatör","Bir grup sohbetini kolaylaştır ya da yönet.",3);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KATEGORİ 1 — İŞ
    // ══════════════════════════════════════════════════════════════════════

    static { // Bölüm 1 — Masa
        add(1,1,"Odaya Gir","Toplantı odasına zamanında girin, bir yere oturun, bekleyin.",1);
        add(1,1,"Not Al","Toplantıda konuşanı dinleyerek not alın, başka bir şey yapmayın.",1);
        add(1,1,"Baş Sallayın","Birisi konuşurken başınızı sallayarak dinlediğinizi gösterin.",1);
        add(1,1,"Onaylayın","Birinin görüşüne 'Evet, katılıyorum.' diyerek destek verin.",1);
        add(1,1,"Soruya Yanıt","Size doğrudan yöneltilen bir soruyu kısaca yanıtlayın.",2);
        add(1,1,"Kısa Yorum","Toplantıda en az bir cümlelik bir yorum yapın.",2);
        add(1,1,"Soru Sor","Bir konuyu netleştirmek için soru sorun.",2);
        add(1,1,"Fikir Ekle","Devam eden tartışmaya kendi fikrinizi ekleyin.",2);
        add(1,1,"Farklı Görüş","Saygılı biçimde farklı bir bakış açısı paylaşın.",3);
        add(1,1,"Eylem Öner","Bir konuda somut bir adım ya da eylem önerin.",3);
        add(1,1,"Konu Aç","Gündemdeki yeni bir konuyu siz başlatın.",3);
        add(1,1,"Geri Bildirim","Bir iş arkadaşının sunumuna yapıcı geri bildirim verin.",3);
        add(1,1,"Üç Kez Söz","Aynı toplantıda en az üç kez söz alın.",3);
        add(1,1,"Özet Yap","Toplantının sonuna doğru tartışmayı siz özetleyin.",3);
        add(1,1,"Toplantıyı Kapat","Bir sonraki adımları siz belirleyip paylaşın.",3);
    }

    static { // Bölüm 2 — Söz
        add(1,2,"Sesli Prova","Sunumunuzu yalnız başınıza sesli olarak prova edin.",1);
        add(1,2,"Aynada Prova","Aynada kendinize bakarak sunumunuzu yapın.",1);
        add(1,2,"Giriş Cümlesi","Sunumun açılış cümlesini yazın ve sesli çalışın.",1);
        add(1,2,"Bir Kişiye Sun","Güvendiğiniz birine sunum yapın ve geri bildirim isteyin.",2);
        add(1,2,"Küçük Ekip","3-4 kişilik küçük bir gruba sunum yapın.",2);
        add(1,2,"Zamanlama","Sunumu süresinde bitirmeyi pratik yapın.",2);
        add(1,2,"Göz Teması","Sunum sırasında farklı kişilere göz teması kurun.",2);
        add(1,2,"Soru-Cevap","Sunumdan sonra en az bir soruyu yanıtlayın.",3);
        add(1,2,"Notsuz Konuş","Notlara bakmadan en az bir dakika konuşun.",3);
        add(1,2,"Jest & Mimik","Sunum yaparken ellerinizi ve yüz ifadenizi kullanın.",3);
        add(1,2,"Beklenmedik Soru","Beklenmedik bir soruya hazırlıksız cevap verin.",3);
        add(1,2,"Bağlantı Kur","Kişisel bir deneyim paylaşarak dinleyiciyle bağ kurun.",3);
        add(1,2,"Tam Sunum","Başından sonuna kadar eksiksiz bir sunum yapın.",3);
        add(1,2,"Güçlü Bitiş","Sunumu güçlü ve net bir kapanış cümlesiyle bitirin.",3);
        add(1,2,"Büyük Grup","10+ kişilik bir gruba bağımsız bir sunum yapın.",3);
    }


    static { // Bölüm 3 — Ağ
        add(1,3,"LinkedIn Gözle","Sektörünüzdeki birinin LinkedIn profilini inceleyin.",1);
        add(1,3,"Online Yorum","Bir profesyonelin paylaşımına kısa bir yorum bırakın.",1);
        add(1,3,"Mesaj At","Tanıdığınız birine kısa bir 'merhaba' mesajı gönderin.",1);
        add(1,3,"Etkinliğe Git","Bir mesleki etkinliğe veya seminere katılın.",2);
        add(1,3,"İlk Tanışma","Etkinlikte ilk siz merhaba deyin ve kendinizi tanıtın.",2);
        add(1,3,"İletişim Paylaş","Birisinin iletişim bilgilerini alın ya da paylaşın.",2);
        add(1,3,"Ortak Konu","Mesleki bir etkinlikte biriyle ortak bir konu üzerine konuşun.",2);
        add(1,3,"İki Kişiyle Tanış","Aynı etkinlikte iki farklı kişiyle tanışın.",3);
        add(1,3,"Takip Mesajı","Tanıştığınız birine LinkedIn üzerinden takip veya teşekkür mesajı gönderin.",3);
        add(1,3,"Panelde Söz","Bir panel ya da açık etkinlikte soruya ses verin.",3);
        add(1,3,"İlgi Alanı Paylaş","Mesleki ilginizi veya kariyer hedefinizi birine aktarın.",3);
        add(1,3,"Webinar Sorusu","Online bir etkinlikte sohbet kutusuna soru yazın.",3);
        add(1,3,"Referans İste","Güvendiğiniz birinden mesleki bir referans talep edin.",3);
        add(1,3,"Mentor İste","Sizi ilham veren birine mentor olmasını teklif edin.",3);
        add(1,3,"Ağ Haritası","Mesleki ağınızı kâğıda çizin ve kiminle bağlantı kurabileceğinizi belirleyin.",3);
    }

    static { // Bölüm 4 — Sahne
        add(1,4,"Araştır","Başvurduğunuz şirket hakkında en az 5 bilgi edinin.",1);
        add(1,4,"CV Sesli Oku","CV'nizi sesli okuyun ve güçlü yanlarınızı belirleyin.",1);
        add(1,4,"Erken Bağlan","Görüşme yerine ya da linke zamanından erken bağlanın.",1);
        add(1,4,"Sık Sorular","'Kendinizden bahsedin' sorusuna sesli cevap verin.",2);
        add(1,4,"Aynada Prova","Sık sorulan görüşme sorularına aynada prova yapın.",2);
        add(1,4,"Güçlü Yanlar","En az üç güçlü yanınızı ve birer örneğini hazırlayın.",2);
        add(1,4,"Beden Dili","Görüşme sırasında dik oturmayı ve göz temasını bilinçli kullanın.",2);
        add(1,4,"Soru Sor","Görüşmeciye şirkete dair hazırladığınız bir soru sorun.",3);
        add(1,4,"Zayıf Yana Cevap","'Zayıf yönünüz nedir?' sorusuna dürüst ve gelişim odaklı cevap verin.",3);
        add(1,4,"Mock Interview","Bir arkadaşınızla sahte görüşme yapın.",3);
        add(1,4,"Maaş Konuşması","Maaş beklentinizi nasıl ifade edeceğinizi sesli çalışın.",3);
        add(1,4,"İlk 60 Saniye","Görüşmenin ilk dakikasında sorulan soruya özgüvenle cevap verin.",3);
        add(1,4,"Başarı Hikâyesi","STAR yöntemiyle bir başarı hikâyesi anlatın.",3);
        add(1,4,"Teşekkür Maili","Görüşme sonrası teşekkür e-postası gönderin.",3);
        add(1,4,"Reddi Kabul Et","Ret cevabı aldığınızda kibarca geri bildirim isteyin.",3);
    }

    static { // Bölüm 5 — Teklif
        add(1,5,"Kâğıda Yaz","Öne süreceğiniz fikri madde madde yazın.",1);
        add(1,5,"Tek Kişiye","Güvendiğiniz bir iş arkadaşınıza fikrinizi anlatın.",1);
        add(1,5,"E-posta ile","Bir fikri e-posta yoluyla yöneticinize iletin.",2);
        add(1,5,"Gerekçe Sun","Fikrinizin neden işe yarayacağını açıklayın.",2);
        add(1,5,"Örnek Göster","Fikrinizi destekleyen bir örnek ya da veri paylaşın.",2);
        add(1,5,"Toplantıda Dile","Bir toplantıda fikrinizi paylaşın.",3);
        add(1,5,"Eleştiriye Hazırlan","Fikrinize gelen eleştiriyi sakin karşılayın ve yanıtlayın.",3);
        add(1,5,"Ortak Bul","Fikrinize destek verecek bir iş arkadaşı bulun.",3);
        add(1,5,"Yazılı Öneri","Fikrinizi resmi bir öneri belgesi olarak hazırlayın.",3);
        add(1,5,"Sunum Yap","Fikrinizi bir slayt ile sunun.",3);
        add(1,5,"Güncelle","Geri bildirime göre fikrinizi geliştirin.",3);
        add(1,5,"Yöneticiye Sun","Fikrinizi doğrudan yöneticinize sunun.",3);
        add(1,5,"Sonucu Takip Et","Fikrinizin durumunu bir hafta sonra takip edin.",3);
        add(1,5,"Beyin Fırtınası","Ekiple beyin fırtınasına önderlik edin.",3);
        add(1,5,"Pilot Öneri","Fikrinizi küçük bir pilot uygulama olarak önerin.",3);
    }

    static { // Bölüm 6 — Sınır
        add(1,6,"Hayır Prova","Aynada 'Hayır, şu an için bu uygun değil.' cümlesini çalışın.",1);
        add(1,6,"Yazılı Hayır","Bir talebe e-posta ile nazikçe hayır deyin.",1);
        add(1,6,"Küçük Red","Gereksiz bir toplantı davetini nazikçe reddedin.",2);
        add(1,6,"Alternatif Sun","Hayır derken alternatif bir öneri sunun.",2);
        add(1,6,"Önceliklendirme","'Bunun önceliklerimle uyuşmuyor.' diyerek reddedin.",2);
        add(1,6,"Fazla Mesai","Sürekli gelen fazla mesai talebini kibar ama net biçimde reddedin.",2);
        add(1,6,"Açıklama Yok","Gerekçe açıklamadan nazikçe hayır demeyi deneyin.",3);
        add(1,6,"Yüz Yüze Hayır","Bir isteği yüz yüze reddeden siz olun.",3);
        add(1,6,"Sınır Belirle","İş-özel hayat sınırınızı netleştirin ve bunu birisiyle paylaşın.",3);
        add(1,6,"Tekrar Gelen Talep","Defalarca gelen gereksiz bir talebi reddedin.",3);
        add(1,6,"Üste Hayır","Yöneticinizin uygunsuz bir isteğini kibar ama net biçimde reddedin.",3);
        add(1,6,"Kalabalıkta Hayır","Grup ortamında bir talebe hayır deyin.",3);
        add(1,6,"Sınırı Koru","Hayır dedikten sonra ısrar karşısında pozisyonunuzu koruyun.",3);
        add(1,6,"Erken Hayır","Talebi dinler dinlemez erken hayır deyin.",3);
        add(1,6,"Hayır'ı Onayla","Kendi 'hayır'ınızı doğru ve yeterli kabul edin.",3);
    }

    static { // Bölüm 7 — Çatışma
        add(1,7,"Hazırlık","Yapacağınız zor konuşmayı önceden yazılı olarak planlayın.",1);
        add(1,7,"Duygu Adı","Yaşadığınız duyguyu kendi kendinize adlandırın.",1);
        add(1,7,"Ben Dili","'Sen şöyle yapıyorsun' yerine 'Ben böyle hissediyorum' deyin.",2);
        add(1,7,"Geri Bildirim Ver","Bir iş arkadaşına yapıcı bir geri bildirim verin.",2);
        add(1,7,"Geri Bildirim İste","Performansınız hakkında geri bildirim isteyin.",2);
        add(1,7,"Hata Bil","Bir hata konusunda sorumluluk alın ve bunu konuşun.",3);
        add(1,7,"Beklenti Netleştir","İş beklentilerinizi netleştirmek için yöneticinizle görüşün.",3);
        add(1,7,"Proje Uyuşmazlığı","Bir projede fikir ayrılığını dile getirin ve çözüm önerin.",3);
        add(1,7,"Maaş Talep Et","Maaş zammı talebini yöneticinizle konuşun.",3);
        add(1,7,"Çatışma Çöz","İş arkadaşıyla yaşadığınız bir gerginliği yüz yüze konuşarak çözün.",3);
        add(1,7,"Hakkı Savun","Haksız bir uygulamaya itiraz edin.",3);
        add(1,7,"Konuşma Başlat","'Seninle konuşmam gereken bir şey var.' diyerek başlatın.",3);
        add(1,7,"Anlaşıldı mı","Konuşmanın sonunda karşı tarafın sizi anlayıp anlamadığını sorun.",3);
        add(1,7,"Teşekkür Et","Zor bir konuşmayı dinlediği için içtenlikle teşekkür edin.",3);
        add(1,7,"Sonuç Odaklı","Konuşmayı 'ne yapabiliriz?' sorusuyla bitirin.",3);
    }


    static { // Bölüm 8 — Yön
        add(1,8,"Gönüllü Ol","Bir toplantıda ya da projede gönüllü olarak öncü rolü alın.",1);
        add(1,8,"Yetki Ver","Bir görevi başkasına delege edin ve güvenin.",2);
        add(1,8,"Takımı Motive Et","Ekibinize cesaretlendirici bir mesaj ya da söz iletin.",2);
        add(1,8,"Başarıyı Kutla","Ekibin başarısını açıkça takdir edin ve kutlayın.",2);
        add(1,8,"Mentorluk","Daha az deneyimli bir iş arkadaşına rehberlik edin.",3);
        add(1,8,"Yol Haritası","Bir proje için net bir yol haritası çizin ve paylaşın.",3);
        add(1,8,"Geri Bildirim Kültürü","Ekipte açık geri bildirim kültürünü başlatın.",3);
        add(1,8,"Vizyon Paylaş","Ekibinize uzun vadeli bir vizyon ya da hedef paylaşın.",3);
        add(1,8,"Çatışma Yönet","Ekip içi bir çatışmaya arabulucu olun.",3);
        add(1,8,"Zor Karar","Ekip adına tartışmalı bir karar alın ve bunu açıklayın.",3);
        add(1,8,"Hesap Ver","Bir hata karşısında sorumluluk kabul edin ve çözüm önerin.",3);
        add(1,8,"Sunumu Devral","Beklenmedik bir anda sunumu ya da toplantıyı devralmak zorunda kalın.",3);
        add(1,8,"Kriz Yönetimi","Ani bir sorun karşısında sakin kalarak ekibe yön gösterin.",3);
        add(1,8,"Şeffaf Ol","Ekibinizle güçlü ve zayıf yanlarınızı açıkça paylaşın.",3);
        add(1,8,"Manifesto","Kendiniz için bir liderlik ilkeleri listesi yazın ve en az biriyle paylaşın.",3);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KATEGORİ 2 — OKUL
    // ══════════════════════════════════════════════════════════════════════

    static { // Bölüm 1 — El
        add(2,1,"Hazırlan","Dersten önce sorabileceğiniz bir soru ya da yorum hazırlayın.",1);
        add(2,1,"Prova","Aynada el kaldırıp cevap vermeyi canlandırın.",1);
        add(2,1,"Doğru Cevap","Bildiğiniz bir soruya el kaldırıp cevap verin.",1);
        add(2,1,"Onay Ver","Birinin cevabına 'Evet, ben de öyle düşünüyorum.' diyerek destek verin.",2);
        add(2,1,"Netleştir","Anlamadığınız bir konuyu sormak için el kaldırın.",2);
        add(2,1,"Tartışmada Dahil","Sınıf tartışmasında en az bir kez söz alın.",2);
        add(2,1,"İlk El","Derste ilk soruyu siz sorun.",2);
        add(2,1,"Ek Bilgi","El kaldırıp konuya ek bir bilgi ya da bakış açısı ekleyin.",3);
        add(2,1,"Devam Sorusu","Bir önceki cevabı takip eden ikinci bir soru sorun.",3);
        add(2,1,"Zor Soru","Zor ya da derinlikli bir soru sorun.",3);
        add(2,1,"Çelişen Fikir","Hocaya ya da sınıfa farklı bir görüş bildirin.",3);
        add(2,1,"Yanıt Tamamla","Eksik kalan bir cevabı siz tamamlayın.",3);
        add(2,1,"İkinci El","Aynı derste iki kez el kaldırın.",3);
        add(2,1,"Önder Ol","Hoca sormadan siz söz alın.",3);
        add(2,1,"Beş Kez","Haftada beş farklı derste en az bir kez söz alın.",3);
    }

    static { // Bölüm 2 — Koridor
        add(2,2,"Gülümse","Sınıfta tanımadığınız birine gülümseyin.",1);
        add(2,2,"Selam Ver","Dersten önce ya da sonra yanınızdaki birine merhaba deyin.",1);
        add(2,2,"Adını Öğren","Derste ya da koridorda birinin adını öğrenin.",1);
        add(2,2,"Ders Sorusu","Bir sınıf arkadaşına ödev ya da ders hakkında bir şey sorun.",2);
        add(2,2,"Mola Sohbeti","Teneffüste ya da molada biriyle kısa bir sohbet yapın.",2);
        add(2,2,"Ortak İlgi","Biriyle ortak bir hobinizi ya da ilginizi keşfedin.",2);
        add(2,2,"Birlikte Çalış","Kütüphanede ya da kafede biriyle aynı masada ders çalışın.",3);
        add(2,2,"Beraber Otur","Kafeteryada yalnız oturan birine katılmayı teklif edin.",3);
        add(2,2,"Ders Arkadaşı","Ders çalışmak için biriyle randevulaşın.",3);
        add(2,2,"İletişim Paylaş","Yeni tanıştığınız biriyle numara ya da sosyal medya paylaşın.",3);
        add(2,2,"Grup Sohbeti","Küçük bir grupta konuşmaya dahil olun.",3);
        add(2,2,"Ortak Mekân","Birinin nerelerde zaman geçirdiğini sorup ortak mekân bulun.",3);
        add(2,2,"Randevu Kur","Bir sınıf arkadaşınızla kahve için buluşma teklif edin.",3);
        add(2,2,"Kulübe Katıl","Yeni bir öğrenci kulübü ya da topluluğuna katılın.",3);
        add(2,2,"Üç Arkadaş","Haftada üç farklı sınıf arkadaşıyla kısa bir sohbet yapın.",3);
    }

    static { // Bölüm 3 — Hoca
        add(2,3,"Teşekkür Et","Dersten sonra hocaya 'Teşekkür ederim.' deyin.",1);
        add(2,3,"E-posta","Hocaya kibar bir e-posta gönderin.",1);
        add(2,3,"Soru Sor","Dersten sonra hocaya bir konuyu sorun.",2);
        add(2,3,"Ofis Saati","Hocayla ofis saatinde görüşün.",2);
        add(2,3,"Zorluğu Anlat","Anlamakta güçlük çektiğiniz konuyu hocaya açıkça bildirin.",2);
        add(2,3,"Geri Bildirim İste","Bir ödeviniz ya da sınavınız hakkında geri bildirim isteyin.",3);
        add(2,3,"Öneri Alın","Okul hayatınız ya da kariyer planınız için önerisini sorun.",3);
        add(2,3,"Projeyi Paylaş","Kişisel bir projenizi ya da ilginizi hocayla paylaşın.",3);
        add(2,3,"Araştırma Sorusu","Hocanın araştırma alanıyla ilgili soru sorun.",3);
        add(2,3,"Beyin Fırtınası","Hocanızla bir projeniz için beyin fırtınası yapın.",3);
        add(2,3,"Görüş Ayrılığı","Hocayla saygılı biçimde farklı bir görüş paylaşın.",3);
        add(2,3,"Nota İtiraz","Haksız bir notla ilgili saygılı biçimde itiraz edin.",3);
        add(2,3,"Referans İste","Başvurunuz için hocadan referans isteyin.",3);
        add(2,3,"Destek İste","Akademik ya da kişisel bir zorluk için hocadan destek isteyin.",3);
        add(2,3,"Teşekkür Mesajı","Dönem sonunda hocaya samimi bir teşekkür mesajı gönderin.",3);
    }

    static { // Bölüm 4 — Kürsü
        add(2,4,"Konuyu Belirle","Sunum konunuzu seçin ve ana noktaları listeleyin.",1);
        add(2,4,"Sesli Prova","Sunumunuzu yüksek sesle prova edin.",1);
        add(2,4,"Açılış Cümlesi","Güçlü bir açılış cümlesini sesli çalışın.",1);
        add(2,4,"Bir Kişiye Sun","Sunumunuzu bir arkadaşınıza yapın.",2);
        add(2,4,"Zamanlama","Sunumunuzu süresinde bitirmeyi pratik yapın.",2);
        add(2,4,"Doğru Kıyafet","Sunuma uygun giyinin ve beden dilinizi düzenleyin.",2);
        add(2,4,"Göz Teması","Sunum sırasında farklı kişilere göz teması kurun.",3);
        add(2,4,"Notsuz Konuş","Notlara bakmadan en az bir dakika konuşun.",3);
        add(2,4,"Soru-Cevap","Sunumdan sonra en az bir soruyu yanıtlayın.",3);
        add(2,4,"Mizah Kullan","Sunuma uygun bir espri ya da anekdot ekleyin.",3);
        add(2,4,"Teknik Sorun","Sunum sırasında teknik bir sorunla sakin kalarak başa çıkın.",3);
        add(2,4,"Geri Bildirim Al","Sunumdan sonra sınıf arkadaşlarından geri bildirim isteyin.",3);
        add(2,4,"Bağlantı Kur","Kişisel bir deneyim paylaşarak dinleyiciyle bağ kurun.",3);
        add(2,4,"Güçlü Kapanış","Sunumu akılda kalıcı bir cümleyle kapatın.",3);
        add(2,4,"Tam Sunum","Sınıfın önünde başından sonuna eksiksiz bir sunum yapın.",3);
    }

    static { // Bölüm 5 — Yemekhane
        add(2,5,"Tek Gir","Kafeteryaya yalnız girin ve bir şey alın.",1);
        add(2,5,"Sırada Bekle","Kalabalık kafeterya sırasında sabırla bekleyin.",1);
        add(2,5,"Kahve Molası","Yalnız kahve içerken ortamı gözlemleyin, telefona bakmayın.",1);
        add(2,5,"Rutini Kır","Her zamanki yeriniz yerine farklı bir yere oturun.",2);
        add(2,5,"Rahat Olmayan Yer","Sizi rahat hissettirmeyen bir yere bilinçli oturun.",2);
        add(2,5,"Yan Masaya Selam","Yakınınızda oturan birine selam verin.",2);
        add(2,5,"Yemek Öneri","Görevliden ya da birinden yemek önerisi alın.",2);
        add(2,5,"Görevliyle Konuş","Kafeterya görevlisiyle kısa bir sohbet yapın.",2);
        add(2,5,"Gürültüde Kal","Kalabalık ve gürültülü kafeteryada en az 20 dakika kalın.",3);
        add(2,5,"Yalnız Birine Katıl","Yalnız oturan birine yanına oturabilir misiniz diye sorun.",3);
        add(2,5,"Yeni Kişiyle Ye","Farklı biriyle aynı masada öğle yemeği yiyin.",3);
        add(2,5,"Konu Aç","Birlikte otururken sohbet başlatın.",3);
        add(2,5,"Grup Masası","Grup masasına katılmayı teklif edin.",3);
        add(2,5,"Grupla Uğra","Bir grup etrafında dolaşın, konuşmaya katılın.",3);
        add(2,5,"Beraber Plan","Tanıştığınız biriyle sonraki buluşmayı planlayın.",3);
    }

    static { // Bölüm 6 — Takım
        add(2,6,"Katıl","Gruba dahil olun ve ne yapıldığını dinleyin.",1);
        add(2,6,"Görev Al","Gruptan bir görev veya sorumluluk üstlenin.",1);
        add(2,6,"Teşekkür Et","Grup üyelerinin katkısını takdir edin.",2);
        add(2,6,"Son Kontrol","Grubun çalışmasını teslim öncesi kontrol edin.",2);
        add(2,6,"Fikir Sun","Bir çalışma grubunda fikrinizi paylaşın.",2);
        add(2,6,"Sorgula","Bir noktayı netleştirmek için soru sorun.",2);
        add(2,6,"Eksiği Tamamla","Grubun atladığı bir noktayı tamamlayın.",3);
        add(2,6,"Öneri Savun","Fikrinizi eleştiri karşısında gerekçeyle savunun.",3);
        add(2,6,"Geri Bildirim Ver","Bir grup üyesine yapıcı geri bildirim verin.",3);
        add(2,6,"Taslak Hazırla","Grup için bir çalışma planı ya da taslak oluşturun.",3);
        add(2,6,"Online Katıl","Online grup çalışmasına kamerası açık katılın.",3);
        add(2,6,"Koordine Et","Görevlerin dağıtımında koordinasyonu siz üstlenin.",3);
        add(2,6,"Sunum Rolü","Grup sunumunda en az bir bölümü siz sunun.",3);
        add(2,6,"Çatışma Çöz","Grupta fikir ayrılığında arabulucu olun.",3);
        add(2,6,"Liderliği Dene","Grup çalışmasında liderlik rolünü üstlenin.",3);
    }

    static { // Bölüm 7 — Sahne
        add(2,7,"Etkinlik Bul","Okul etkinlik panosunu ya da duyuru sayfasını inceleyin.",1);
        add(2,7,"Arkadaşla Git","Bir arkadaşınızı etkinliğe davet edin ve birlikte gidin.",1);
        add(2,7,"Kaydol","Bir kulüp ya da etkinliğe kayıt yaptırın.",2);
        add(2,7,"Sosyal Medya","Etkinliği sosyal medyada paylaşın ya da yorum yapın.",2);
        add(2,7,"Yalnız Git","Bir okul etkinliğine yalnız başınıza gidin.",2);
        add(2,7,"Tanışın","Etkinlikte yeni biriyle tanışın.",3);
        add(2,7,"Oy Kullanın","Öğrenci toplantısında oy ya da görüş bildirin.",3);
        add(2,7,"Söz Alın","Etkinlik sırasında söz alın ya da soru sorun.",3);
        add(2,7,"Kulübe Katıl","Bir kulüp toplantısına ilk kez katılın.",3);
        add(2,7,"Yeni Etkinlik","Daha önce katılmadığınız türden bir etkinliğe katılın.",3);
        add(2,7,"Gönüllü Ol","Bir etkinlikte görev alın — kayıt, sunum, dekor vb.",3);
        add(2,7,"Organizasyona Katıl","Bir etkinliğin organizasyonunda gönüllü olun.",3);
        add(2,7,"Öneri Ver","Etkinlik organizatörlerine geri bildirim ya da öneri iletin.",3);
        add(2,7,"Sahneye Çıkın","Drama, müzik ya da sunum etkinliğinde sahne alın.",3);
        add(2,7,"Etkinlik Kur","Kendi başlatmak istediğiniz bir etkinlik ya da kulüp için fikir geliştirin.",3);
    }

    static { // Bölüm 8 — Kulüp
        add(2,8,"Kulüp Araştır","Okulunuzdaki ya da çevrenizde katılabileceğiniz kulüpleri araştırın.",1);
        add(2,8,"İlk Toplantı","Bir kulübün toplantısına ilk kez katılın, sadece dinleyin.",1);
        add(2,8,"İsim Öğren","Toplantıda yanınızdaki birinin adını öğrenin.",1);
        add(2,8,"Görüş Bildir","Kulüp toplantısında kısa bir görüş bildirin.",2);
        add(2,8,"Gönüllü Ol","Kulüp için küçük bir görevi üstlenin.",2);
        add(2,8,"Etkinlik Öner","Kulübe bir etkinlik ya da aktivite önerin.",2);
        add(2,8,"Sosyal Paylaş","Kulübün etkinliğini sosyal medyada paylaşın.",2);
        add(2,8,"Yeni Üye Getir","Bir arkadaşınızı kulübe davet edin.",3);
        add(2,8,"Sorumluluk Al","Kulüpte düzenli bir sorumluluk üstlenin.",3);
        add(2,8,"Fikir Savun","Önerdiğiniz fikri grup içinde savunun.",3);
        add(2,8,"Organizasyonu Yönet","Bir kulüp etkinliğinin organizasyonunu üstlenin.",3);
        add(2,8,"Konuşmacı Ol","Kulüp toplantısında kısa bir sunum ya da konuşma yapın.",3);
        add(2,8,"Liderliğe Aday","Kulüpte bir liderlik pozisyonuna aday olun ya da önerilmeyi kabul edin.",3);
        add(2,8,"Farklı Kulüp","Daha önce hiç düşünmediğiniz farklı bir kulübe katılın.",3);
        add(2,8,"Kendi Kulübü Kur","Olmayan ama ihtiyaç duyulan bir kulüp için fikir geliştirin ve adım atın.",3);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KATEGORİ 3 — MAHALLE
    // ══════════════════════════════════════════════════════════════════════

    static { // Bölüm 1 — Bakkal
        add(3,1,"İçeri Gir","Mahalledeki bir bakkala ya da küçük markete girin, bir şey alın.",1);
        add(3,1,"Ürün Sor","Çalışana bir ürünün nerede olduğunu sorun.",1);
        add(3,1,"Günaydın","Bakkala girerken 'Günaydın' ya da 'İyi günler' deyin.",1);
        add(3,1,"Fiyat Sor","Bir ürünün fiyatını kasiyere sorun.",1);
        add(3,1,"Teşekkür","Kasiyere alışveriş sonunda içtenlikle teşekkür edin.",1);
        add(3,1,"İndirim Sor","Kampanya veya indirim olup olmadığını sorun.",2);
        add(3,1,"Alternatif Sor","Aradığınız ürün yoksa alternatif sorun.",2);
        add(3,1,"İki Soru","Aynı çalışana arka arkaya iki farklı soru sorun.",2);
        add(3,1,"Yeni Ürün","Daha önce denemediğiniz bir ürünü çalışana sorarak keşfedin.",2);
        add(3,1,"Öneri İste","Çalışandan ürün önerisi isteyin.",2);
        add(3,1,"Düz Yap","Esnafa sembolik bir indirim isteyin — 'Düz yapabilir miyiz?'",3);
        add(3,1,"Şikâyet Et","Eksik ya da bozuk bir ürünü çalışana bildirin.",3);
        add(3,1,"Kasiyere Sohbet","Kasiyerle alışveriş sırasında kısa bir sohbet başlatın.",3);
        add(3,1,"Tanıdık Ol","Aynı bakkala üç gün üst üste gidin, her gün biraz daha tanıdık davranın.",3);
        add(3,1,"İsimle Hitap","Bakkalın ya da kasiyerin adını öğrenin ve ismiyle hitap edin.",3);
    }

    static { // Bölüm 2 — Kapı
        add(3,2,"Selam Ver","Koridorda ya da kapıda karşılaştığınız komşuya selam verin.",1);
        add(3,2,"Kapı Tut","Komşunuz için kapıyı tutun.",1);
        add(3,2,"Asansör Selamı","Asansörde komşunuzla karşılaşınca selam verip gülümseyin.",1);
        add(3,2,"İsim Öğren","Komşunuzun adını öğrenin.",2);
        add(3,2,"Kısa Sohbet","Komşuyla hava veya semt hakkında kısa bir sohbet yapın.",2);
        add(3,2,"Kapıcıyla Konuş","Bina görevlisiyle kısa bir sohbet yapın.",2);
        add(3,2,"İki Komşu","Farklı iki komşuyla aynı gün selamlaşın.",2);
        add(3,2,"Çocukla Tanış","Komşunuzun çocuğuyla ya da evcil hayvanıyla tanışın.",2);
        add(3,2,"Kapı Zili","Gerekli bir konuda komşunuzun kapısını çalın.",3);
        add(3,2,"Ortak Sorun","Binada ortak bir sorunu komşuyla konuşun.",3);
        add(3,2,"Yardım Teklif","Komşunuza alışveriş ya da taşıma konusunda yardım önerin.",3);
        add(3,2,"Yeni Komşu","Yeni taşınan komşuyu selamlayın, kısa tanışma yapın.",3);
        add(3,2,"Küçük Jest","Komşuya küçük bir yiyecek ya da jest yapın.",3);
        add(3,2,"Toplantıya Katıl","Site ya da bina sakinleri toplantısına katılın.",3);
        add(3,2,"Çaya Davet","Komşunuzu çaya ya da kahveye davet edin.",3);
    }

    static { // Bölüm 3 — Park
        add(3,3,"Parka Git","Parkta yalnız başınıza en az 15 dakika geçirin, telefonsuz.",1);
        add(3,3,"Bankta Otur","Boş bir banka oturun ve etrafı izleyin.",1);
        add(3,3,"Fotoğraf İste","Birinden sizin fotoğrafınızı çekmesini isteyin.",2);
        add(3,3,"Yol Tarifi","Birine ya da birinden yol tarifi verin ya da alın.",2);
        add(3,3,"Hava Yorumu","Yanınızdaki birine 'Hava çok güzel değil mi?' deyin.",2);
        add(3,3,"Köpek Sahibi","Köpeğiyle gelen birine hayvanı hakkında bir şey sorun.",2);
        add(3,3,"Bankta Yer","Dolmuş bir bankta birine 'Yanınıza oturabilir miyim?' deyin.",2);
        add(3,3,"Etkinlik Sor","Parkta ya da semtte etkinlik olup olmadığını sorun.",2);
        add(3,3,"Yerel Bilgi","Semtin en iyi kafesi ya da restoranını sorun.",2);
        add(3,3,"Kitap Sorusu","Kitap okuyan birine hangi kitabı okuduğunu sorun.",3);
        add(3,3,"Spontane Selam","Herhangi bir bahane olmadan yalnız oturan birine selam verin.",3);
        add(3,3,"Spor Konuşma","Parkta egzersiz yapan biriyle antrenman hakkında konuşun.",3);
        add(3,3,"Çocuk Ailesi","Çocuğunu getiren birine kısa bir sohbet başlatın.",3);
        add(3,3,"Birlikte Yürü","Kısa bir süre parkta biriyle yan yana yürüyün.",3);
        add(3,3,"On Dakika Sohbet","Parkta tanıştığınız biriyle 10 dakika sohbet edin.",3);
    }

    static { // Bölüm 4 — Durak
        add(3,4,"Binin","Toplu taşımaya binin ve en az 2 durak gidin.",1);
        add(3,4,"Nazik Pardon","Kalabalıkta nazikçe 'Pardon' diyerek geçin.",1);
        add(3,4,"Yer Aç","Yaşlı ya da hamile birine yer açın.",1);
        add(3,4,"Durak Sor","Durağa gelen birine 'Bu hat buradan geçiyor mu?' diye sorun.",2);
        add(3,4,"Bilet Sor","Bilete ya da ücrete dair birinden bilgi alın.",2);
        add(3,4,"Yanınızdakine Selam","Yanınıza oturan birine selam verin.",2);
        add(3,4,"Kayıp Eşya","Birinin düşürdüğü bir şeyi kaldırıp verin.",2);
        add(3,4,"Farklı Hat","Daha önce binmediğiniz bir hat ya da güzergâhı deneyin.",2);
        add(3,4,"Durağı Teyit Et","İnmek istediğiniz durağı şoför ya da biriyle teyit edin.",2);
        add(3,4,"Müzisyen","Toplu taşımada müzisyeni dinleyin, göz teması kurun ya da alkışlayın.",2);
        add(3,4,"Gürültüde Kal","Kalabalık ve gürültülü toplu taşımada rahat kalmayı deneyin.",3);
        add(3,4,"Yol Yardımı","Yolunu şaşırmış birine yardım edin.",3);
        add(3,4,"Sohbet Başlat","Toplu taşımada birisiyle kısa bir sohbet başlatın.",3);
        add(3,4,"Şoförle Konuş","Minibüste şoförle kısa bir sohbet yapın.",3);
        add(3,4,"Aktarma","Aktarmalı bir güzergâhı tercih edin ve yabancı ortamda rahat olun.",3);
    }

    static { // Bölüm 5 — Çay
        add(3,5,"Kafe Seç","Daha önce girmediğiniz bir kafe seçin.",1);
        add(3,5,"Sipariş Ver","Çalışana siparişinizi verin.",1);
        add(3,5,"Vedada Teşekkür","Ayrılırken çalışana 'Teşekkürler, eline sağlık' deyin.",1);
        add(3,5,"Yalnız Otur","Yalnız oturup çayınızı ya da kahvenizi için, telefonsuz.",2);
        add(3,5,"Menü Sorusu","Menüdeki bir ürünü sorun ya da öneri isteyin.",2);
        add(3,5,"Fatura İste","Faturayı sesli olarak isteyin.",2);
        add(3,5,"Masa Değiştir","Rahat olmayan bir yerde otururken masa değiştirmek için izin isteyin.",2);
        add(3,5,"Kalabalık Kafe","Kalabalık bir kafede en az 20 dakika oturun.",2);
        add(3,5,"Barista İsmi","Baristayla ismini öğrenerek hitap edin.",3);
        add(3,5,"Şikâyet Bildir","Soğuk ya da hatalı sipariş için nazikçe bildirin.",3);
        add(3,5,"Yan Masaya Selam","Yakın masada oturan birine gülümseyin ya da selam verin.",3);
        add(3,5,"Çalışanla Sohbet","Kafe çalışanıyla kısa bir sohbet başlatın.",3);
        add(3,5,"Gürültüde Çalış","Kalabalık kafede bir şeyler çalışın ya da okuyun.",3);
        add(3,5,"Kafe Rutini","Aynı kafeye üç kez gidip tanışık hissini deneyin.",3);
        add(3,5,"Çay Daveti","Tanıdığınız birine 'Bir çay içelim mi?' diye teklif edin.",3);
    }

    static { // Bölüm 6 — Meydan
        add(3,6,"Etkinlik Bul","Mahallenizde ya da şehrinizde bir etkinlik araştırın.",1);
        add(3,6,"Erken Gidin","Etkinliğe erken giderek ortamı keşfedin.",1);
        add(3,6,"Yalnız Git","Bir etkinliğe yalnız gidin.",2);
        add(3,6,"Sosyal Paylaşım","Etkinliği paylaşın ya da fotoğraf çekin.",2);
        add(3,6,"Davet Et","Bir arkadaşı ya da tanıdığı etkinliğe davet edin.",2);
        add(3,6,"Tanışın","Etkinlikte yeni biriyle tanışın.",3);
        add(3,6,"Yeni Tür","Daha önce katılmadığınız bir tür etkinliğe gidin.",3);
        add(3,6,"Soru Sorun","Bir panel ya da konuşmada soru sorun.",3);
        add(3,6,"Gönüllü Olun","Organizasyonun bir bölümünde yardımcı olun.",3);
        add(3,6,"Sanatçıyla Konuş","Etkinlikte bir konuşmacı ya da sanatçıyla iki cümle konuşun.",3);
        add(3,6,"İletişim Paylaş","Etkinlikte tanıştığınız biriyle iletişim bilgilerini paylaşın.",3);
        add(3,6,"İki Etkinlik","Aynı hafta içinde iki farklı etkinliğe katılın.",3);
        add(3,6,"Geri Bildirim","Organizasyona geri bildirim ya da teşekkür gönderin.",3);
        add(3,6,"Değerlendirin","Etkinlik sonrası deneyiminizi bir kişiyle paylaşın.",3);
        add(3,6,"Öncülük Et","Bir mahalle etkinliği ya da buluşması için fikir geliştirin ve adım atın.",3);
    }

    static { // Bölüm 7 — İstek
        add(3,7,"Küçük Rica","Birinden küçük bir şey isteyin — kalem, bozuk para, ateş.",1);
        add(3,7,"Yol Tarifi","Bildiğiniz bir yeri bile olsa birinden yol tarifi isteyin.",1);
        add(3,7,"Kapıyı Tut","Birinin kapıyı tutmasını bekleyin, koşmayın.",1);
        add(3,7,"Fotoğraf","Birinden sizin fotoğrafınızı çekmesini isteyin.",2);
        add(3,7,"Esnaf Tavsiye","Mahalledeki esnaftan tavsiye isteyin — en iyi ürün, en güzel lezzet.",2);
        add(3,7,"Eczacıdan Bilgi","Eczacıdan bir ürün ya da ilaç hakkında bilgi alın.",2);
        add(3,7,"Komşudan Ödünç","Komşunuzdan küçük bir şey ödünç isteyin — tuz, şeker, alet.",2);
        add(3,7,"Yardımı Kabul Et","Size teklif edilen bir yardımı 'Yok hayır gerek' demeden kabul edin.",2);
        add(3,7,"Şarj İste","Kafede ya da bir yerde telefonunuzu şarj etmek için izin isteyin.",2);
        add(3,7,"Masa Sor","Kalabalık bir yerde boş masa olup olmadığını sorun.",2);
        add(3,7,"Ağır Yük","Ağır bir şeyi taşırken yardım isteyin.",3);
        add(3,7,"Geri Bildirim","Bir işiniz ya da projeniz için güvendiğiniz birinden geri bildirim isteyin.",3);
        add(3,7,"Duygusal Destek","Güvendiğiniz birinden duygusal destek isteyin.",3);
        add(3,7,"İki İstek","Aynı gün iki farklı kişiden yardım isteyin.",3);
        add(3,7,"Sesli Kabul","'Yardıma ihtiyacım var.' demekten çekinmeden bir kişiye söyleyin.",3);
    }

    static { // Bölüm 8 — Semt
        add(3,8,"Semti Keşfet","Mahallenizde daha önce girmediğiniz bir dükkana ya da mekâna girin.",1);
        add(3,8,"Esnaf Selamı","Mahalledeki bir esnafı her gün selamlayın, bir hafta boyunca.",1);
        add(3,8,"Yerel Bilgi","Semtteki en iyi yer hakkında birine tavsiye verin.",2);
        add(3,8,"Fikir Paylaş","Mahalle ya da site WhatsApp grubunda bir fikir paylaşın.",2);
        add(3,8,"Sorun Bildir","Semtteki bir sorunu muhtara ya da ilgili kişiye bildirin.",2);
        add(3,8,"Tanışma Teklifi","Daha önce sadece selam verdiğiniz birine 'Bir çay içelim mi?' deyin.",3);
        add(3,8,"Söz Al","Mahalle ya da site toplantısında söz alın.",3);
        add(3,8,"Yabancıya Yardım","Semtte yolunu şaşırmış ya da yardıma ihtiyacı olan birine yardım edin.",3);
        add(3,8,"Dükkancıyla Sohbet","Mahalledeki bir esnafla alışveriş dışında sohbet edin.",3);
        add(3,8,"Etkinlik Öner","Mahalle grubuna ya da komşulara bir buluşma önerisi yapın.",3);
        add(3,8,"Temizlik Eylemi","Çevrede küçük bir iyileştirme ya da temizlik eylemi başlatın.",3);
        add(3,8,"İki Yabancı","Aynı günde iki farklı mahalle sakiniyle sohbet edin.",3);
        add(3,8,"Uzun Sohbet","Mahalledeki biriyle 10 dakikadan uzun bir sohbet yapın.",3);
        add(3,8,"Minnet Göster","Mahallenizde size yardımı dokunan birine teşekkür edin.",3);
        add(3,8,"Tanınan Biri Ol","Bu hafta mahallenizde en az 5 farklı kişiyle anlamlı bir temas kurun.",3);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KATEGORİ 4 — ROMANTİK
    // ══════════════════════════════════════════════════════════════════════

    static { // Bölüm 1 — Farkında Olmak
        add(4,1,"Çekim Günlüğü","İlginizi çeken biri hakkında düşüncelerinizi bir kâğıda yazın.",1);
        add(4,1,"Tip Tanımla","Kendinizi çeken insan tipini üç kelimeyle tanımlayın.",1);
        add(4,1,"Hayal Et","İdeal bir buluşmayı zihinsel olarak hayal edin.",1);
        add(4,1,"Engeli Yaz","Romantik alanda sizi en çok durduran şeyi yazın.",1);
        add(4,1,"Gözlem Yap","Bugün çevrenizde ilginizi çeken biri var mı? Sadece fark edin.",2);
        add(4,1,"Beden Dilini Oku","Birine ilgi duyan birinin beden dilini gözlemleyin.",2);
        add(4,1,"Sinyali Fark Et","Birisinin size ilgi gösterdiği bir anı hatırlayın ve yazın.",2);
        add(4,1,"İçten Tepki","İlginizi çeken biri yanınızdan geçince ne hissediyorsunuz? Yazın.",2);
        add(4,1,"Ortam Tara","Bir kafede ya da parkta çevrenizi bilinçli olarak gözlemleyin.",2);
        add(4,1,"Korku Listesi","Romantik alanda yaşadığınız en büyük 3 korkuyu listeleyin.",2);
        add(4,1,"Prova Yap","İlginizi çeken biriyle tanışma sahnesini zihinsel olarak canlandırın.",3);
        add(4,1,"Geçmişi İncele","Daha önce ilgi gösterdiğiniz birini neden adım atmadığınızı yazın.",3);
        add(4,1,"Güçlü Yanlar","Romantik ilişkide ne sunabileceğinizi üç maddeyle yazın.",3);
        add(4,1,"Engeli Sorgula","'Bu korku gerçek mi, yoksa hikâye mi?' diye kendinize sorun.",3);
        add(4,1,"Başlangıç Kutlaması","Bu bölümü tamamladığınız için kendinizi yazılı olarak kutlayın.",3);
    }

    static { // Bölüm 2 — Göz Teması ve İlk Sinyal
        add(4,2,"Aynada Pratik","Aynaya bakarak 3 saniye göz teması kurun ve gülümseyin.",1);
        add(4,2,"Tanıdık Yüz","Düzenli gördüğünüz birine (bakkal, komşu) gülümseyin.",1);
        add(4,2,"Güvenli Ortam","Kalabalık olmayan bir yerde birine göz teması kurun.",1);
        add(4,2,"Gülümseme Dene","Bugün üç farklı yabancıya kısa ve doğal bir gülümseme gönderin.",2);
        add(4,2,"2 Saniye Temas","İlginizi çeken birine 2 saniye göz teması kurun.",2);
        add(4,2,"Beklemeyin","Göz temasını karşı taraf başlatana kadar beklemeyin, siz başlatın.",2);
        add(4,2,"Tepkiyi Gözlemle","Göz temasına gülümseyen biriyle bir saniye daha bakışın.",2);
        add(4,2,"Loş Ortamda","Gece veya loş bir mekânda birine göz teması kurun.",2);
        add(4,2,"Kaçmayın","Göz temasında kaçmak yerine sakin kalın ve devam edin.",3);
        add(4,2,"İkinci Temas","Kısa aralıklarla aynı kişiyle iki kez göz teması kurun.",3);
        add(4,2,"Grup Ortamı","Grup içinde ilginizi çeken birine göz teması kurun.",3);
        add(4,2,"Uzaktan Sinyal","Uzaktan biriyle göz teması kurun ve hafifçe başınızı eğin.",3);
        add(4,2,"Süreyi Uzat","3 saniyeyi aşan bir göz teması kurun ve sakin kalın.",3);
        add(4,2,"Kasıtlı Bak","'Görmek' değil 'göstermek' için göz teması kurun.",3);
        add(4,2,"Bugünü Değerlendir","Bugünkü göz temalarını ve tepkileri bir yere not edin.",3);
    }

    static { // Bölüm 3 — İlk Söz
        add(4,3,"Cümle Yaz","İlk sohbeti başlatacak üç farklı cümleyi kâğıda yazın.",1);
        add(4,3,"Sesli Prova","O cümleleri yüksek sesle, aynaya karşı söyleyin.",1);
        add(4,3,"Güvenli Kişi","Yakın çevrenizden biriyle küçük bir konuşma başlatın.",1);
        add(4,3,"Ortam Yorumu","Bulunduğunuz mekânla ilgili kısa bir yorum yapın.",2);
        add(4,3,"Nesne Üzerine","Birinin kitabı veya aksesuarı üzerine samimi bir yorum yapın.",2);
        add(4,3,"Soru Sor","Doğal bir soruyla sohbet başlatın.",2);
        add(4,3,"5 Saniye Kuralı","Tereddüt başladığında 5'ten geriye sayıp konuşun.",2);
        add(4,3,"Kısa Tut","İlk sözü tek cümleyle bitirin; fazla açıklamaya gerek yok.",2);
        add(4,3,"Beklenmedik Yer","Alışveriş, ulaşım ya da bekleme gibi alışılmadık bir yerde konuşun.",3);
        add(4,3,"Sohbeti Sürdür","İlk soruya aldığınız yanıtla konuşmayı iki cümle daha ilerletin.",3);
        add(4,3,"Espri Dene","Hafif ve duruma uygun bir espriyle sohbet açın.",3);
        add(4,3,"Soğukluğu Kabul","Karşı taraf ilgisiz olsa da sakin ve nazik kalın.",3);
        add(4,3,"İsim Öğren","Sohbeti ilerletip 'Ben [isim]im, sen?' diye sorun.",3);
        add(4,3,"Kendinizden Ekle","Sohbete kendinizle ilgili küçük bir şey katın.",3);
        add(4,3,"Nasıl Hissettiniz","Konuştuktan sonra o anı ve hislerinizi yazın.",3);
    }

    static { // Bölüm 4 — İletişim Bilgisi İstemek
        add(4,4,"Senaryo Yaz","Numara ya da sosyal medya isterken ne söyleyeceğinizi yazın.",1);
        add(4,4,"Arkadaşla Prova","Bu sahneyi güvendiğiniz biriyle canlandırın.",2);
        add(4,4,"Sosyal Medya","Numara yerine sosyal medya hesabı isteyin.",2);
        add(4,4,"Hayır Provosu","'Hayır' cevabını makul karşılamayı zihinsel olarak prova yapın.",2);
        add(4,4,"Neden Belirtin","İletişim bilgisi isterken somut bir neden gösterin.",2);
        add(4,4,"Kısa & Net","'Numaranı alabilir miyim?' cümlesini direkt söyleyin.",3);
        add(4,4,"Net İfade","'Seninle tekrar görüşmek isterim.' diyerek isteyin.",3);
        add(4,4,"Siz Verin","Beklemek yerine siz numaranızı ya da hesabınızı önce verin.",3);
        add(4,4,"Spontane Dene","Plan yapmadan, anlık bir anda isteyin.",3);
        add(4,4,"Hayır Karşısında","Hayır yanıtı alınca sakin bir şekilde teşekkür edip ayrılın.",3);
        add(4,4,"Aldıktan Sonra","Aldığınız numaraya kısa bir 'merhaba' mesajı gönderin.",3);
        add(4,4,"İki Kez Dene","İki farklı günde iki farklı kişiden iletişim bilgisi isteyin.",3);
        add(4,4,"Reddedildikten Sonra","Reddedildiğiniz gün başka biriyle tekrar deneyin.",3);
        add(4,4,"Uyumsuzluk Hatırlat","Her 'hayır' bir red değil, uyumsuzluktur — bunu yazın.",2);
        add(4,4,"Denediğinizi Kutlayın","Sonuç ne olursa olsun, istediniz — bu yeterli.",2);
    }

    static { // Bölüm 5 — Buluşma Teklifi
        add(4,5,"Senaryo Yaz","Kahve ya da çıkma teklifini nasıl yapacağınızı yazın.",1);
        add(4,5,"Sesli Prova","Teklifi yüksek sesle prova yapın.",1);
        add(4,5,"Hayır Provosu","Hayır yanıtını nazikçe kabul etmeyi prova yapın.",2);
        add(4,5,"Net Sor","'Kahve içmek ister misin?' diye doğrudan sorun.",2);
        add(4,5,"Online Teklif","Mesajla bir buluşma teklif edin.",2);
        add(4,5,"Zaman Önerin","'Bu hafta sonu müsait misin?' gibi somut bir zaman belirtin.",3);
        add(4,5,"Yer Önerin","Kahvenin yanı sıra somut bir mekân önerin.",3);
        add(4,5,"Doğal Bağlam","Sohbet içinde konuya uygun bir noktada teklif edin.",3);
        add(4,5,"Ortak İlgiyle","Paylaştığınız ortak bir ilgiyi bahane ederek buluşmayı önerin.",3);
        add(4,5,"Etkinliğe Davet","Bir etkinliğe, sergiye ya da konsere birlikte gitmeyi önerin.",3);
        add(4,5,"Ertelemeye Yanıt","Erteleme yapan birine uygun bir zamanda tekrar teklif edin.",3);
        add(4,5,"Kahveye Gidin","Teklif ettiğiniz buluşmayı gerçekleştirin.",3);
        add(4,5,"Doğal Sohbet","Buluşmada konuşmayı zorlamadan, doğal akışına bırakın.",3);
        add(4,5,"Sonucu Yazın","Buluşmanın nasıl geçtiğini gözlemleyip bir yere yazın.",2);
        add(4,5,"Cesareti Kutlayın","Gittiğiniz için kendinizi tebrik edin.",2);
    }

    static { // Bölüm 6 — Duyguları İfade Etmek
        add(4,6,"Duygu Günlüğü","Birine karşı hissettiklerinizi bir kâğıda yazın.",1);
        add(4,6,"İçten Takdir","Biriyle konuşurken samimi bir takdirde bulunun.",2);
        add(4,6,"Olumlu Bildir","'Bu beni çok mutlu etti.' gibi olumlu bir duyguyu söyleyin.",2);
        add(4,6,"Ben Dili","'Ben böyle hissediyorum.' cümle yapısını kullanın.",2);
        add(4,6,"Teşekkür","Birinin size kattığı şey için içtenlikle teşekkür edin.",2);
        add(4,6,"Etkiyi Söyleyin","'Seninle vakit geçirmek beni iyi hissettiriyor.' demeyi deneyin.",3);
        add(4,6,"Beğeniyi Söyle","Birinin belirli bir özelliğini doğrudan ve içtenlikle ifade edin.",3);
        add(4,6,"Yazılı İfade","Bir duygunuzu kısa bir mesaj olarak gönderin.",3);
        add(4,6,"İlgiyi Belirt","Birine ilgi duyduğunuzu net ama sakin biçimde aktarın.",3);
        add(4,6,"Savunmasız Ol","Kendinizi açmaktan çekindiğiniz bir duyguyu paylaşın.",3);
        add(4,6,"Korkuyu Kabul","'Bu beni biraz korkutuyor ama söylemek istedim.' demeyi deneyin.",3);
        add(4,6,"Sınır Koy","'Bu davranış beni rahatsız etti.' diyebileceğiniz bir durumu dile getirin.",3);
        add(4,6,"Karşılıklı Paylaşım","Karşı tarafın da duygusunu paylaşmasına zemin hazırlayın.",3);
        add(4,6,"Güçtür Hatırlat","'Duygularımı ifade etmek zayıflık değil, cesaret.' yazın.",2);
        add(4,6,"Paylaşımı Kutla","Duygu paylaştığınız her an için kendinizi kutlayın.",2);
    }

    static { // Bölüm 7 — Reddedilmeyle Yüzleşmek
        add(4,7,"Korkuyu Yaz","Reddedilme korkusunu bir kâğıda somutlaştırın.",1);
        add(4,7,"En Kötü Senaryo","En kötü ne olabilir? Gerçekçi biçimde yazın.",1);
        add(4,7,"Kasti Red","Küçük bir istekte 'hayır' cevabı almayı bilerek deneyin.",2);
        add(4,7,"Duyguyu Adla","Reddedilince hissettiğiniz duyguyu adlandırın.",2);
        add(4,7,"Sakin Kal","'Hayır' karşısında sakin kalın ve teşekkür edin.",2);
        add(4,7,"Anlamlandırın","Reddin sizi değil, uyumsuzluğu ifade ettiğini yazın.",2);
        add(4,7,"Gülümseme Veda","Hayır yanıtından sonra gülümseyerek nazikçe ayrılın.",3);
        add(4,7,"Paylaşın","Bir reddedilme deneyimini güvendiğiniz biriyle paylaşın.",3);
        add(4,7,"Tekrar Dene","Reddedildikten sonra farklı bir kişiyle aynı gün tekrar deneyin.",3);
        add(4,7,"Hız Kazan","Bir hafta içinde iki kez 'hayır' almayı bilinçli hedefleyin.",3);
        add(4,7,"Hayat Değişmedi","Reddedilmenin hayatınızı değiştirmediğini kendinize kanıtlayın.",3);
        add(4,7,"Kendinizi Onaylayın","'Denedim, bu yeterli.' diyerek kendinizi onaylayın.",2);
        add(4,7,"Büyümeyi Yaz","Reddedilmenin sizi nasıl güçlendirdiğini bir yere yazın.",2);
        add(4,7,"Beklentiyi Bırak","Sonuca değil, eyleme odaklanın.",2);
        add(4,7,"Bölümü Kutla","Bu bölümü tamamlamak için kendinizi kutlayın.",2);
    }

    static { // Bölüm 8 — İlişki Sınırları
        add(4,8,"Sınır Listesi","Kendiniz için önemli olan sınırları listeleyin.",1);
        add(4,8,"Değer Belirle","İlişkide vazgeçemeyeceğiniz üç değeri yazın.",1);
        add(4,8,"Hayır Provosu","Romantik bir bağlamda hayır demeyi zihinsel olarak prova yapın.",2);
        add(4,8,"Değer Paylaşımı","Önemli bir değerinizi yeni tanıştığınız biriyle paylaşın.",2);
        add(4,8,"Zaman Sınırı","Ne kadar zaman harcamak istediğinizi belirleyin ve buna uyun.",2);
        add(4,8,"Kişisel Alan","Kişisel alana ihtiyaç duyduğunuzda bunu dile getirin.",3);
        add(4,8,"Baskıya Hayır","Sizi zorlayan bir baskı karşısında sakin biçimde hayır deyin.",3);
        add(4,8,"Sınırı Bildirin","Rahatsız edici bir davranış karşısında sınırınızı net ifade edin.",3);
        add(4,8,"Beklenti Netleştir","İlişkiden beklentilerinizi sakin bir ortamda paylaşın.",3);
        add(4,8,"İletişim Sınırı","Mesaj sıklığı ya da iletişim biçimiyle ilgili sınırınızı belirtin.",3);
        add(4,8,"Saygı Talep","Saygısızlık karşısında sakin ama net konuşun.",3);
        add(4,8,"Suçlamayın","Sınır koyduktan sonra kendinizi suçlamayın.",2);
        add(4,8,"Ortak Sınır","İlişkideki ortak sınırları karşılıklı konuşun.",3);
        add(4,8,"Sınır = Saygı","Sınır koymanın sevgisizlik değil, saygı olduğunu yazın.",2);
        add(4,8,"Yolculuğu Kutla","Bu kategoriyi tamamladığınız için kendinizi kutlayın.",2);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  KATEGORİ 5 — AİLE
    // ══════════════════════════════════════════════════════════════════════

    static { // Bölüm 1 — Aile Dinamiklerini Anlamak
        add(5,1,"Aile Haritası","Aile üyelerini ve kendinizle ilişkinizi kısaca yazın.",1);
        add(5,1,"Güçlü Yan","Ailenizle ilişkinizde güçlü olan bir yönü yazın.",1);
        add(5,1,"Zorluk Yaz","Ailenizle iletişimde en zor bulduğunuz şeyi yazın.",1);
        add(5,1,"Duygu Haritası","Her aile üyesiyle ilgili aklınıza ilk gelen duyguyu yazın.",1);
        add(5,1,"Kalıpları Fark Et","Aile içinde tekrarlayan bir iletişim kalıbını fark edin.",2);
        add(5,1,"Rol Sorgula","Ailede üstlendiğiniz rolü (arabulucu, çatışmacı, sessiz kalan) tanımlayın.",2);
        add(5,1,"İhtiyaç Listesi","Aile ilişkisinde daha çok neye ihtiyacınız olduğunu yazın.",2);
        add(5,1,"Olumlu An","Bir aile üyesiyle yaşadığınız olumlu bir anı yazın.",2);
        add(5,1,"Tekrar Eden Çatışma","Ailede sık yaşanan bir çatışmayı nesnel olarak tanımlayın.",2);
        add(5,1,"Beklenti Yaz","Ailenizden beklentilerinizi somut biçimde listeleyin.",3);
        add(5,1,"Empati Dene","Bir aile üyesinin bakış açısından durumu yeniden yazın.",3);
        add(5,1,"Değiştiremeyeceğiniz","Ailenizde değiştiremeyeceğiniz ama kabul edebileceğiniz bir şeyi yazın.",3);
        add(5,1,"Değiştirebileceğiniz","Kendi davranışlarınızda değiştirebileceğiniz bir şeyi yazın.",3);
        add(5,1,"Hedef Belirle","Bu kategoride neyi geliştirmek istediğinizi tek cümleyle yazın.",3);
        add(5,1,"Farkındalık Kutla","Bu bölümü tamamlamak için kendinizi kutlayın.",2);
    }

    static { // Bölüm 2 — Günlük İletişimi Güçlendirmek
        add(5,2,"Selam Ver","Bir aile üyesine özenli ve samimi bir selam verin.",1);
        add(5,2,"Teşekkür Et","Küçük bir şey için içtenlikle teşekkür edin.",1);
        add(5,2,"Nasılsın Sor","Birine 'Nasılsın?' deyip cevabı gerçekten dinleyin.",1);
        add(5,2,"Günlük Paylaşım","Sofrada veya birlikte olduğunuz anda gününüzden bir şey anlatın.",2);
        add(5,2,"Yorum Ekle","Devam eden bir konuşmaya fikrinizi sakin biçimde ekleyin.",2);
        add(5,2,"Konu Açın","Masada ya da evde yeni bir konuyu siz başlatın.",2);
        add(5,2,"Espri Yapın","Aile ortamında hafif bir espri ya da eğlenceli bir şey paylaşın.",2);
        add(5,2,"Dinleyin","Birini sonuna kadar kesmeden dinleyin.",2);
        add(5,2,"Görüş Ekleyin","Bir konuda farklı düşündüğünüzü saygılı biçimde belirtin.",3);
        add(5,2,"Özür Dileyin","Gerekiyorsa samimi bir 'Özür dilerim.' deyin.",3);
        add(5,2,"Üç Kez Söz Alın","Aynı oturumda üç kez konuşmaya katılın.",3);
        add(5,2,"Sohbeti Bitirin","Bir konuşmayı doğal ve nazik biçimde kapatın.",2);
        add(5,2,"Plan Önerin","Aile için bir hafta sonu planı önerin.",2);
        add(5,2,"Hikâye Anlatın","Aileye eğlenceli bir anı ya da hikâye anlatın.",2);
        add(5,2,"Bugünü Değerlendirin","Bu günkü iletişimde neyin iyi gittiğini yazın.",2);
    }

    static { // Bölüm 3 — Büyüklerle Derin Bağ
        add(5,3,"Özenli Selam","Büyüklere özenli bir selam verin ve hal hatır sorun.",1);
        add(5,3,"Nasılsın Dinle","Büyük birine 'Nasılsınız?' deyip cevabı gerçekten dinleyin.",1);
        add(5,3,"Yardım Teklif","Günlük bir konuda büyüklere yardımcı olun.",1);
        add(5,3,"Anı Dinle","Büyük birini anılarını anlatırken dikkatle ve sabırla dinleyin.",2);
        add(5,3,"Merak Sor","Yaşam deneyimleri hakkında samimi bir soru sorun.",2);
        add(5,3,"Geçmişe Merak","Aile tarihinden bir şeyi sorup öğrenin.",2);
        add(5,3,"Öğüt Alın","Bir konuda büyüklerden içtenlikle tavsiye isteyin.",2);
        add(5,3,"Fikir Paylaşın","Kendi görüşünüzü büyüklerle paylaşın.",3);
        add(5,3,"Farklı Görüş","Büyüklerle saygılı biçimde fikir ayrılığı yaşayın.",3);
        add(5,3,"Baş Başa Vakit","Büyük bir aile üyesiyle baş başa vakit geçirin.",3);
        add(5,3,"Arama Yapın","Uzakta olan büyükleri arayarak hal hatır sorun.",2);
        add(5,3,"Teşekkür Et","Büyüklerden birine destekleri için içtenlikle teşekkür edin.",3);
        add(5,3,"Sevgi İfadesi","Büyüklerinize sevgi ya da saygınızı kelimelerle ifade edin.",3);
        add(5,3,"Ortak Aktivite","Büyüklerle birlikte bir aktivite yapın.",3);
        add(5,3,"Bağı Kutlayın","Bu bölümü tamamlamak için kendinizi kutlayın.",2);
    }

    static { // Bölüm 4 — Duygularını Paylaşmak
        add(5,4,"Günlük Yaz","Aile ilişkisine dair hissettiklerinizi bir günlüğe yazın.",1);
        add(5,4,"Duyguyu Adla","Aileyle ilgili hissettiğiniz bir duyguyu net olarak adlandırın.",1);
        add(5,4,"Olumlu Paylaşım","Bir aile üyesine olumlu bir duygunuzu bildirin.",2);
        add(5,4,"Teşekkür Et","Birinin katkısı için içten teşekkür edin.",2);
        add(5,4,"Ben Dili","'Ben böyle hissettim.' cümle yapısını bir konuşmada kullanın.",2);
        add(5,4,"Sevinç Paylaşımı","Mutlu bir anı ya da duyguyu aileyle paylaşın.",2);
        add(5,4,"Duygu Odaklı","'Sen hep böyle yapıyorsun.' yerine 'Ben bu durumdan etkilendim.' deyin.",3);
        add(5,4,"Üzüntü Paylaşımı","Üzüldüğünüzü bir aile üyesiyle paylaşın.",3);
        add(5,4,"Endişeyi Söyle","Endişe duyduğunuz bir şeyi bir aile üyesine anlatın.",3);
        add(5,4,"Sessizliği Boz","Uzun süredir konuşmadığınız bir konuyu açın.",3);
        add(5,4,"Saygı Talep","Saygısız bir davranışı nazikçe ama net biçimde bildirin.",3);
        add(5,4,"Özür Dileyin","Gerekiyorsa samimi bir özür dileyin.",3);
        add(5,4,"Affetme","Birini affettiğinizi içtenlikle yaşayın veya söyleyin.",3);
        add(5,4,"Minnettarlık","'Senin için şükranım var.' diyebileceğiniz bir anı paylaşın.",3);
        add(5,4,"Cesareti Kutla","Bu adımı attığınız için kendinizi kutlayın.",2);
    }

    static { // Bölüm 5 — Aileye Hayır Demek
        add(5,5,"Prova","Aileye hayır demeyi zihinsel olarak canlandırın.",1);
        add(5,5,"Sınır Listesi","Aile ilişkisinde önemli olan sınırlarınızı yazın.",1);
        add(5,5,"Küçük Red","Ufak bir isteği nazikçe reddedin.",2);
        add(5,5,"Yumuşak Hayır","'Şu an için bu benim için uygun değil.' demeyi deneyin.",2);
        add(5,5,"Alternatif Sun","Hayır derken alternatif bir çözüm önerin.",2);
        add(5,5,"Suçlamayın","Hayır dedikten sonra kendinizi suçlamayın.",2);
        add(5,5,"Açıklama Yok","Gerekçe açıklamadan nazikçe hayır deyin.",3);
        add(5,5,"Sınır Belirtin","Sizi zorlayan bir istekte sınırınızı bildirin.",3);
        add(5,5,"Baskı Karşısında","Baskı karşısında da pozisyonunuzu koruyun.",3);
        add(5,5,"Büyüğe Hayır","Bir büyüğün isteğini saygılı biçimde reddedin.",3);
        add(5,5,"Tekrarlayan Talep","Defalarca gelen aynı isteğe net bir cevap verin.",3);
        add(5,5,"İhtiyaçları Savun","Kendi ihtiyaçlarınızın da önemli olduğunu hatırlatın.",3);
        add(5,5,"Hayır Sonrası","'Hayır' dedikten sonra ilişkinin sürdüğünü gözlemleyin.",3);
        add(5,5,"Sınır = Sağlık","Sınır koymanın sağlıklı ilişkinin göstergesi olduğunu yazın.",2);
        add(5,5,"Kutlayın","Bu görevi tamamlamak için kendinizi kutlayın.",2);
    }

    static { // Bölüm 6 — Anlaşmazlıkta Konuşmak
        add(5,6,"Duyguyu Yaz","Anlaşmazlık yaşadığınız konuyu ve hislerinizi bir kâğıda yazın.",1);
        add(5,6,"Ben Dili","'Sen hep' yerine 'Ben bu durumda...' diye başlayın.",2);
        add(5,6,"Zaman İsteyin","'Buna şimdi cevap vermek istemiyorum.' diyebilmeyi deneyin.",2);
        add(5,6,"Dinleyin","Karşı tarafı kesmeden sonuna kadar dinleyin.",2);
        add(5,6,"Anlamaya Çalışın","Karşı tarafın neden öyle düşündüğünü anlamaya çalışın.",3);
        add(5,6,"Özetleyin","Karşı tarafın söylediklerini kendi kelimelerinizle özetleyin.",3);
        add(5,6,"Sakin Kalın","Tartışma sırasında sakin kalmayı bilinçli olarak deneyin.",3);
        add(5,6,"Takdirle Başlayın","Anlaşmazlığa girmeden önce karşı tarafı takdir edin.",3);
        add(5,6,"Yükselmeyi Durdurun","Sesiniz yükselince bilinçli olarak yavaşlayın.",3);
        add(5,6,"Çözüm Önerin","Anlaşmazlığa çözüm odaklı yaklaşın.",3);
        add(5,6,"Uzlaşın","Orta yol bulmayı kabul edin.",3);
        add(5,6,"Özür Dileyin","Hatalıysanız içtenlikle özür dileyin.",3);
        add(5,6,"İlişkiye Devam","Tartışma sonrasında ilişkiye devam edin.",3);
        add(5,6,"Öğreneni Yazın","Anlaşmazlıktan ne öğrendiğinizi bir yere yazın.",2);
        add(5,6,"Büyümeyi Kutlayın","Bu görevi tamamladığınız için kendinizi kutlayın.",2);
    }

    static { // Bölüm 7 — Sevgi ve Minnettarlık İfadesi
        add(5,7,"Düşünüyorum","Bir aile üyesine 'Seni düşündüm.' diyerek mesaj gönderin.",1);
        add(5,7,"El Yazısı","Sevgi dolu el yazılı bir not bırakın.",1);
        add(5,7,"Sarılın","Bir aile üyesine sarılın.",2);
        add(5,7,"Teşekkür Et","Birinin desteği için içtenlikle teşekkür edin.",2);
        add(5,7,"Küçük Sürpriz","Birini düşünerek küçük bir sürpriz yapın.",2);
        add(5,7,"Takdirleyin","Birinin belirli bir özelliğini açıkça takdir edin.",2);
        add(5,7,"Birlikte Vakit","Özel bir şey yapmadan sadece birlikte vakit geçirin.",2);
        add(5,7,"Anı Paylaşın","Ortak güzel bir anıyı birlikte konuşun.",2);
        add(5,7,"Seni Seviyorum","Bir aile üyesine 'Seni seviyorum.' deyin.",3);
        add(5,7,"Neden Sevdiklerini Söyle","'Seni neden sevdiğimi anlatayım.' diye başlayın.",3);
        add(5,7,"Her Gün Olumlu","Üç gün boyunca her gün bir aile üyesine olumlu bir şey söyleyin.",3);
        add(5,7,"Arama","Uzakta olan bir aile üyesini salt sevgi için arayın.",3);
        add(5,7,"Önce Siz Arayın","Beklemek yerine önce siz arayın ya da ziyaret edin.",3);
        add(5,7,"Sevgiyi Kabul Et","Size gösterilen sevgiyi küçümsemeden kabul edin.",3);
        add(5,7,"Kutlayın","Bu adımı attığınız için kendinizi kutlayın.",2);
    }

    static { // Bölüm 8 — Aile Liderliği
        add(5,8,"Gözlem Yaz","Ailenizde eksik gördüğünüz bir iletişim unsurunun yazın.",1);
        add(5,8,"Sorumluluk Alın","Ailede bir sorumluluğu gönüllü olarak üstlenin.",2);
        add(5,8,"Söz Tutun","Aileye verdiğiniz bir sözü tutun.",2);
        add(5,8,"Örnek Olun","Davranışlarınızla aileye olumlu bir örnek oluşturun.",2);
        add(5,8,"Destek Sunun","Bir aile üyesinin sorununa çözüm önerisi getirin.",2);
        add(5,8,"Karar Önerin","Aile için bir konuda karar önerin ve gerekçenizi açıklayın.",3);
        add(5,8,"Plan Yapın","Aile için bir etkinlik ya da buluşma planı yapın.",3);
        add(5,8,"Ritüel Önerin","Aile için haftalık bir ritüel ya da gelenek önerin.",3);
        add(5,8,"Çatışmayı Yönet","Aile içi bir gerginlikte arabulucu olun.",3);
        add(5,8,"Açık İletişim","Aile içinde açık iletişim ortamını bilinçli olarak başlatın.",3);
        add(5,8,"Geri Bildirim İste","Aile üyelerinden ilişkiniz hakkında geri bildirim isteyin.",3);
        add(5,8,"Vizyon Paylaşın","Aile için uzun vadeli bir hedef ya da değer paylaşın.",3);
        add(5,8,"Sürpriz Organize","Bir aile üyesi için sürpriz bir etkinlik organize edin.",3);
        add(5,8,"Krizde Sakin Kal","Aile krizinde sakin kalarak yol gösterin.",3);
        add(5,8,"Yolculuğu Kutla","Bu yolculuğu tamamladığınız için kendinizi ve aileyi kutlayın.",3);
    }
}