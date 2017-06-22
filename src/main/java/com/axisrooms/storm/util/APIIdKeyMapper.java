package com.axisrooms.storm.util;


import java.util.HashMap;
import java.util.Map;

public enum APIIdKeyMapper {
    CLEARTRIP(ArcProperties.getCleartripChannelId(), "9f9f15b14feb3963e55f2af93516bcc1", true), TRIPVILLAS(
            PMSProperties.getTripVillasPMSId(), "13c53c566cdc145b5d59bda928a5b584", false), DATADEVICES(PMSProperties
            .getDataDevicesPmsId(), "acfb5eeb8bac29d21bfba88eb80398b7", false), MCUBIC(PMSProperties.getMCuBicPmsId(),
            "7eb228097576abf56968e9845ab51b90", false), INNKEY(PMSProperties.getInnkeyPmsId(),
            "12d2ec2c0a82afaa69d5ceb4fbafaa5f", false), ORAVEL(PMSProperties.getOravelPmsId(),
            "00bb6afa71f2a527287cc8f7a581bac4", false), BITLA(PMSProperties.getBitlaPmsId(),
            "f2853a3c4a7c0b67779c86ff8d51925e", false), CLOUD7(PMSProperties.getCloud7PmsId(),
            "4811b604617f4af188b6431e3fed9de5", false), PERFECTHANDS(PMSProperties.getPerfecthandsPmsId(),
            "36e63070ae37791fb2f9ab72f33dc230", false), MICROGENN(PMSProperties.getMicrogennPmsId(),
            "4438304673c66b1b770d3245c304fa2d", false), FLUSHROOM(PMSProperties.getFlushroomPmsId(),
            "8f99671aec3ac6c686a5f9f7007a8708", false), CLERKHOTEL(PMSProperties.getClerkhotelPmsId(),
            "fb17135045a714b38654d8d6c345e8d4", false), DATAMATE(PMSProperties.getDatamatePmsId(),
            "c3c539ae97e8d7b9dc68db32fc2d4d1d", false), Lucid(PMSProperties.getLucidPMSId(),
            "acd1b3d407104ad9d0dbd481dce309ed", false), DISCOVERROOM(PMSProperties.getDiscoverroomPmsId(),
            "a8c77d7376b7c4270447ed4912b34029", false), HPTDC(PMSProperties.getHptdcPmsId(),
            "8a34547d171d8672baddfedfed862c79", false), SOULITUDE(PMSProperties.getSoulitudePmsId(),
            "57fa0519009700be00433846b2f7d542", false), INDIUM(PMSProperties.getIndiumId(),
            "deffd9389e11f4a95aed626975b8b951", false), IMANAGEMYHOTEL(PMSProperties.getImanagemyhotelPmsId(),
            "5a872f7ad20f79943677426e17777f05", false), ZoRooms(PMSProperties.getZoroomsPmsId(),
            "afe14d32572cd570d056f42b1b3d07e7", false), ASIATECH(PMSProperties.getAsiatechPmsId(),
            "ebb526eddb3f1681c447f2dfd69fdd07", false), GOAVILLA(PMSProperties.getGoavillaPmsId(),
            "d1153b8e978353ee955af81106c090a4", false), OYOROOMS(PMSProperties.getOyoroomsPmsId(),
            "c9dfa3376d8eeed093242a4f7bb389e0", false), ILODGE(PMSProperties.getIlodgePmsId(),
            "0a7f1e49b981717a42bfc3816105ca03", false), SYNCROOMOS(PMSProperties.getSyncroomsPmsId(),
            "7a3bd9548c1a1236475a7e621b020dda", false), LEEFOROOMS(PMSProperties.getLeeforoomsPmsId(),
            "864f6fc6a46075f47d42d54a19c52d7d", false), CUISINELINKS(PMSProperties.getCuisinelinksPmsId(),
            "c1a6dfaea1dae4198d30c1a2cab8302f", false), ROCKETINTERNET(PMSProperties.getRocketinternetPmsId(),
            "48aed19c315fc73defc88f11dbef5334", false), FABHOTELS(PMSProperties.getFabhotelsPmsId(),
            "48be4378b0d069c0aa167bd4a4795206", false), GULJAGINFO(PMSProperties.getGuljaginfoPmsId(),
            "c1ae9bcfb05668fe43ebafbfa343f4ae", false), TRUELOGICS(PMSProperties.getTruelogicsPmsId(),
            "d67b8f7cfb243b146b28d0e24fc9c11c", false), HOSTRA(PMSProperties.getHostraPmsId(),
            "92ef90d6b4f20a046d8376ba6d0cd82d", false), AXRCRS(PMSProperties.getAxrcrsPmsId(),
            "bd2e26a7ff916940ef971f808e3356a8", false), INFOGRID(PMSProperties.getInfogridPmsId(),
            "f0ca0b2825d120d77bd6db05f23d35fb", false), GRACESOFT(PMSProperties.getGracesoftPmsId(),
            "bb753b13276aed3bac96aa6119c4b4bd", false), STARIGLOBAL(PMSProperties.getStariglobalPmsId(),
            "2683623b1212270cd655a7cf98f7a6e5", false), EHORS(PMSProperties.getEhorsPmsId(),
            "81c1a01c69f521fe17372a15cae36e0d", false), BISONWOODS(PMSProperties.getBisonwoodsPmsId(),
            "e32c76595447fae32d99cf870a657cd8", false), VRESORTS(PMSProperties.getVresortsPmsId(),
            "253cd86d971714c2af46908eed1cd3de", false), QIKSTAY(PMSProperties.getQikstayPmsId(),
            "4413d2366f54cce4b7e99913848a8572", false), MOTELDIVINE(PMSProperties.getMoteldivinePmsId(),
            "3e9bb7c60d62e936ec8f3a9eb8e6d48a", false), HOTELOGIX(PMSProperties.getHotelogixPmsId(),
            "1e7dcee3730fb542fe5ad10460c3", false), IDS(PMSProperties.getIdsPmsId(),
            "e0b2c8b6c51f2be1867b2632feac6c5a", false), ORANGECOUNTY(PMSProperties.getOrangecountyPmsId(),
            "8e43c099b0ff6cedd1ddc4ced473e5e6", false), SIMPLOTEL(PMSProperties.getSimplotelPmsId(),
            "7804f6c268d05296e88deef20507bd78", false), AXISROOMS_RM(PMSProperties.getAxisroomsRmPmsId(),
            "e0105ce80331269fd6373f4496e632aa", false), FOURDTECH(PMSProperties.getFourdtechPmsId(),
            "c533d77e796e79f0f46b61ef8924fd9b", false), SMILETRIP(PMSProperties.getSmiletripPmsId(),
            "dfe04eff03b625b11a3fa77be43c6b6c", false), MAYAHOTELS(PMSProperties.getMayahotelsPmsId(),
            "17c5df86c8ebfa374c251f683529bdbe", false), SCIENTER(PMSProperties.getScienterPmsId(),
            "ea919f7d1d944afc40e129a1823dbffd", false), BOOKINGFACTORY(PMSProperties.getBookingFactoryPmsId(), "TEST",
            false), T4TECH(PMSProperties.getT4TechPmsId(), "e0b2c8b6c51f2bt4tech2632feac6c5a", false), BookingWings(
            PMSProperties.getBookingWingsPmsId(), "ea919f7d1booking56wingsa1823dbffd", false), AGH(PMSProperties
            .getAGHPmsId(), "e0b2cgr765c51f2bt4agh2632feac6c5a", false), ZUZUHOTELS(PMSProperties.getZuzuhotelsPmsId(),
            "e0b2c8b6c51f2bzu2zu2632feac6c5a", false), TAURUS(PMSProperties.getTaurusPmsId(),
            "TauRASmbApp-050440257017c53c9c43563fd289f2b4", false), TREEBO(PMSProperties.getTreeboPmsId(),
            "e0b2c8b6c51f2btreebo2632feac6c5a", false), PALMGROVE(PMSProperties.getPalmgrovePmsId(),
            "313db36795684a538bd5d25ba6bdf998", true), EGHAMAT24(PMSProperties.getEghamat24PmsId(),
            "e7649f2fcc48122f02335c9769f61c7b", false);

    private long    m_id;
    private String  m_key;
    private boolean m_ota;

    private APIIdKeyMapper(long id, String key, boolean ota) {
        m_id = id;
        m_key = key;
        m_ota = ota;
    }

    private static Map<Long, APIIdKeyMapper>   s_cacheMap       = new HashMap<>();
    private static Map<String, APIIdKeyMapper> s_cacheMapByName = new HashMap<>();

    static {
        for (APIIdKeyMapper entity : APIIdKeyMapper.values()) {
            s_cacheMap.put(entity.getId(), entity);
            s_cacheMapByName.put(entity.getKey(), entity);
        }
    }

    public static APIIdKeyMapper valueOf(long id) {
        return s_cacheMap.get(id);
    }

    public static APIIdKeyMapper valueByKey(String key) {
        return s_cacheMapByName.get(key);
    }

    public long getId() {
        return m_id;
    }

    public String getKey() {
        return m_key;
    }

    public boolean isOta() {
        return m_ota;
    }

    public void setOta(boolean ota) {
        m_ota = ota;
    }
}
