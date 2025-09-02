package com.qoormthon.empty_wallet.domain.user.entity;

public enum Gender {

  MALE("male"),
  FEMALE("female");

  private final String providerName;

  Gender(String providerName) {
    this.providerName = providerName;
  }

  public String getProviderName() {
    return providerName;
  }

  public static SocialProvider fromString(String providerName) {
    for (SocialProvider provider : SocialProvider.values()) {
      if (provider.getProviderName().equals(providerName)) {
        return provider;
      }
    }
    throw new IllegalArgumentException("Unsupported provider: " + providerName);
  }

}
