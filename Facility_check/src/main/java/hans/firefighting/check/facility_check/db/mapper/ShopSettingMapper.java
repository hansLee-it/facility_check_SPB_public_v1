package hans.firefighting.check.facility_check.db.mapper;

import hans.firefighting.check.facility_check.settings.StationShopDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ShopSettingMapper {
    public List<StationShopDTO> selectShopList(int startIndex);
    public StationShopDTO selectShop(String shopId);
    public StationShopDTO selectAllNames(StationShopDTO stationShop);
    public Integer countTotalShop();
    public Integer insertShop(StationShopDTO shop);
    public Integer updateShopName(StationShopDTO shop);
    public Integer updateShop(StationShopDTO shop);
    public Integer deleteShop(String shopId);
}