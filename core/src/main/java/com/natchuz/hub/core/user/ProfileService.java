package com.natchuz.hub.core.user;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import com.natchuz.hub.core.profile.ProfileRepo;
import com.natchuz.hub.core.user.User;

@Value
public class ProfileService {

    ProfileRepo<User> userRepo;

}
