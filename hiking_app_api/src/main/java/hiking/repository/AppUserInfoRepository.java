package hiking.repository;

import hiking.models.AppUserInfo;

public interface AppUserInfoRepository {

    public AppUserInfo findByAppUserId(int appUserId);
    public AppUserInfo add(AppUserInfo appUserInfo);
    public boolean update(AppUserInfo appUserInfo);
    public boolean delete(int appUserId);
}
