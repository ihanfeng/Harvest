package org.hank.harvest.service;

import org.hank.harvest.domain.UserDetail;

/**
 * Created by Administrator on 2016/5/19.
 */
public interface UserDetailService {

    UserDetail findOne(Integer id);

    UserDetail findOneByUserID(Integer userID);

    Integer saveOne(UserDetail userDetail);

    UserDetail editOne(UserDetail userDetail);

}
