package com.prgrms.catchtable.waiting.service;

import static com.prgrms.catchtable.common.exception.ErrorCode.EXISTING_MEMBER_WAITING;
import static com.prgrms.catchtable.waiting.domain.WaitingStatus.PROGRESS;

import com.prgrms.catchtable.common.exception.custom.BadRequestCustomException;
import com.prgrms.catchtable.member.domain.Member;
import com.prgrms.catchtable.shop.domain.Shop;
import com.prgrms.catchtable.waiting.domain.Waiting;
import com.prgrms.catchtable.waiting.dto.CreateWaitingRequest;
import com.prgrms.catchtable.waiting.dto.CreateWaitingResponse;
import com.prgrms.catchtable.waiting.dto.WaitingMapper;
import com.prgrms.catchtable.waiting.facade.WaitingFacade;
import com.prgrms.catchtable.waiting.repository.WaitingRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WaitingService {
    private final LocalDateTime START_DATE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
    private final LocalDateTime END_DATE_TIME = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));
    private final WaitingRepository waitingRepository;
    private final WaitingFacade waitingFacade;
    public CreateWaitingResponse createWaiting(Long shopId, CreateWaitingRequest request) {
        // 연관 엔티티 조회
        Member member = waitingFacade.getMemberEntity(1L);
        Shop shop = waitingFacade.getShopEntity(shopId);

        // shop 영업 중인지 검증
        shop.validateIfShopOpened(LocalTime.now());

        // 기존 waiting이 있는지 검증
        validateIfMemberWaitingExists(member);

        // 대기 번호 생성
        int waitingNumber = (waitingRepository.countByShopAndStatusAndCreatedAtBetween(shop,
            PROGRESS, START_DATE_TIME, END_DATE_TIME)).intValue()+1;

        // 대기 순서 생성
        int waitingOrder = (waitingRepository.countByShopAndCreatedAtBetween(shop,
            START_DATE_TIME, END_DATE_TIME)).intValue()+1;

        // waiting 저장
        Waiting waiting = WaitingMapper.toWaiting(request, waitingNumber, waitingOrder, member, shop);
        Waiting savedWaiting = waitingRepository.save(waiting);

        return WaitingMapper.toCreateWaitingResponse(savedWaiting);
    }

    private void validateIfMemberWaitingExists(Member member) {
        if (waitingRepository.existsByMember(member)){
            throw new BadRequestCustomException(EXISTING_MEMBER_WAITING);
        }
    }
}
