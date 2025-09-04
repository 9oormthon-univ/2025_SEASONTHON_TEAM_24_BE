package com.qoormthon.empty_wallet.domain.character.service;

import com.qoormthon.empty_wallet.domain.character.entity.CharCode;
import com.qoormthon.empty_wallet.domain.character.entity.Score;
import com.qoormthon.empty_wallet.domain.character.repository.ScoreRepository;
import com.qoormthon.empty_wallet.domain.survey.dto.request.SubmitSurveyRequest;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyOption;
import com.qoormthon.empty_wallet.domain.survey.entity.SurveyType;
import com.qoormthon.empty_wallet.domain.survey.repository.SurveyOptionRepository;
import com.qoormthon.empty_wallet.domain.user.entity.User;
import com.qoormthon.empty_wallet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CharacterScoreService {

    private final SurveyOptionRepository optionRepo;
    private final ScoreRepository scoreRepo;
    private final UserRepository userRepo;

    @Transactional
    public void applySurvey(Long userId, SubmitSurveyRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

        // 1) 캐릭터별 합산
        EnumMap<CharCode, Long> sum = new EnumMap<>(CharCode.class);
        for (CharCode c : CharCode.values()) sum.put(c, 0L);

        Set<Long> answeredSurveyIds = new HashSet<>();
        for (SubmitSurveyRequest.Answer a : req.answers()) {
            SurveyOption opt = optionRepo.findBySurveyIdAndType(a.surveyId(), a.optionType())
                    .orElseThrow(() -> new IllegalArgumentException("OPTION_NOT_FOUND"));
            answeredSurveyIds.add(a.surveyId());

            String[] codes = opt.getCode().split(",");
            int weight = opt.getWeight();              // 예: 21, -11
            int sign = weight < 0 ? -1 : 1;
            String digits = String.valueOf(Math.abs(weight));
            if (digits.length() < codes.length) {      // 자릿수 모자라면 0-패딩
                digits = String.format("%0" + codes.length + "d", Integer.parseInt(digits));
            }
            for (int i = 0; i < codes.length; i++) {
                CharCode code = CharCode.of(codes[i]);
                int d = (i < digits.length()) ? (digits.charAt(i) - '0') : 0;
                sum.put(code, sum.get(code) + sign * d);
            }
        }

        // 2) FULL 동점 보정
        if (req.type() == SurveyType.FULL) {
            long max = sum.values().stream().mapToLong(v -> v).max().orElse(0L);
            int tie = 0;
            for (long v : sum.values()) if (v == max) tie++;
            if (tie >= 2) {
                if (answeredSurveyIds.contains(7L)) {
                    sum.put(CharCode.YOLO, sum.get(CharCode.YOLO) + 1);
                }
                if (answeredSurveyIds.contains(3L) || answeredSurveyIds.contains(9L)) {
                    sum.put(CharCode.FASH, sum.get(CharCode.FASH) + 1);
                    sum.put(CharCode.IMP,  sum.get(CharCode.IMP)  + 1);
                }
            }
        }

        // 3) 점수 반영 (FULL=덮어쓰기, QUICK=누적)
        Score bucket = scoreRepo.findByUser(user)
                .orElseGet(() -> scoreRepo.save(Score.of(user)));

        if (req.type() == SurveyType.FULL) {
            bucket.overwrite(
                    sum.get(CharCode.CAF),
                    sum.get(CharCode.TAX),
                    sum.get(CharCode.IMP),
                    sum.get(CharCode.SUB),
                    sum.get(CharCode.YOLO),
                    sum.get(CharCode.FASH)
            );
        } else { // QUICK
            bucket.addCaf(sum.get(CharCode.CAF));
            bucket.addTax(sum.get(CharCode.TAX));
            bucket.addImp(sum.get(CharCode.IMP));
            bucket.addSub(sum.get(CharCode.SUB));
            bucket.addYolo(sum.get(CharCode.YOLO));
            bucket.addFash(sum.get(CharCode.FASH));
        }

        scoreRepo.save(bucket);
    }

    private void setAll(Score s, EnumMap<CharCode, Long> m) {
        s.addCaf(-s.getCaf());  s.addTax(-s.getTax());  s.addImp(-s.getImp());
        s.addSub(-s.getSub());  s.addYolo(-s.getYolo()); s.addFash(-s.getFash());
        addAll(s, m);
    }

    private void addAll(Score s, EnumMap<CharCode, Long> m) {
        s.addCaf(m.get(CharCode.CAF));
        s.addTax(m.get(CharCode.TAX));
        s.addImp(m.get(CharCode.IMP));
        s.addSub(m.get(CharCode.SUB));
        s.addYolo(m.get(CharCode.YOLO));
        s.addFash(m.get(CharCode.FASH));
    }
}
