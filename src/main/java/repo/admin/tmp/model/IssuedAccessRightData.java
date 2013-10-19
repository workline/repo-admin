package repo.admin.tmp.model;

public class IssuedAccessRightData {
    Long accessRightId;

    Long issuedAccountId;

    Long ownerId;

    public Long getAccessRightId() {
        return accessRightId;
    }

    public void setAccessRightId(Long accessRightId) {
        this.accessRightId = accessRightId;
    }

    public Long getIssuedAccountId() {
        return issuedAccountId;
    }

    public void setIssuedAccountId(Long issuedAccountId) {
        this.issuedAccountId = issuedAccountId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

}
