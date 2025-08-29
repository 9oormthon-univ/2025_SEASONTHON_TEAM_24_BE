package com.qoormthon.empty_wallet.domain.user.service;

public enum SocialProvider {
	KAKAO("kakao"),
	GOOGLE("google");

	private final String providerName;

	SocialProvider(String providerName) {
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
