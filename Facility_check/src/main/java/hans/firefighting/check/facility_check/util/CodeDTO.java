package hans.firefighting.check.facility_check.util;

/**
 * <pre>
 * 1. Class Name : CodeDTO
 * 2. Write Date   : 2024-04-14 오전 2:57
 * 3. Author   : itHans
 * 4. 설명 : instant DTO for data for some tables
 *  "shop","role","code", etc....
 *
 *
 */
public class CodeDTO {
    String id;
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}