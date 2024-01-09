package com.prgrms.catchtable.common.notification;

import static org.springframework.transaction.event.TransactionPhase.*;

import com.prgrms.catchtable.member.domain.Member;
import com.prgrms.catchtable.notification.service.NotificationService;
import com.prgrms.catchtable.owner.domain.Owner;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEvent {

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT) // 호출한쪽의 트랜잭션이 커밋 된 후 이벤트 발생
    public void sentMessage(Member member, NotificationContent content){
        notificationService.sendMessageAndSave(member, content);
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT) // 호출한쪽의 트랜잭션이 커밋 된 후 이벤트 발생
    public void sentMessage(Owner owner, NotificationContent content){
        notificationService.sendMessageAndSave(owner, content);
    }
}
