package hans.firefighting.check.facility_check.settings;

import hans.firefighting.check.facility_check.db.mapper.ShopSettingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopSettingService {

    @Autowired
    ShopSettingMapper shopSettingMapper;

    public List<StationShopDTO> selectShopList(int pageIndex){
        return shopSettingMapper.selectShopList(pageIndex*10);
    }
    public StationShopDTO selectAllNames(StationShopDTO stationShop){
        StationShopDTO result = shopSettingMapper.selectAllNames(stationShop);
        if(result.getLineName() == null) result.setLineId("");
        if(result.getStationName() == null) result.setStationId("");
        if(result.getShopName() == null) result.setShopId("");
        return result;
    }
    public StationShopDTO selectShop(String shopId){
        return shopSettingMapper.selectShop(shopId);
    }
    public Integer countTotalShop(){
        return shopSettingMapper.countTotalShop();
    }
    public Integer insertShop(StationShopDTO shop){
        return shopSettingMapper.insertShop(shop);
    }
    public Integer updateShop(StationShopDTO shop){
        return shopSettingMapper.updateShop(shop);
    }
    public Integer updateShopName(StationShopDTO shop){
        return shopSettingMapper.updateShopName(shop);
    }
    public Integer deleteShop(String shopId){
        return shopSettingMapper.deleteShop(shopId);
    }
}