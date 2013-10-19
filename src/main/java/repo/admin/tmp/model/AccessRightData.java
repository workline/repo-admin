package repo.admin.tmp.model;

public class AccessRightData {
    private String name;

    private EAccessRightType type;

    private Long accountId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EAccessRightType getType() {
        return type;
    }

    public void setType(EAccessRightType type) {
        this.type = type;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

}
