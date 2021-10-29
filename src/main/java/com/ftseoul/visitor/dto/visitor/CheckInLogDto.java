package com.ftseoul.visitor.dto.visitor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ftseoul.visitor.dto.visitor.projection.CheckInVisitor;
import com.ftseoul.visitor.dto.visitor.projection.CheckInVisitorDecrypt;
import com.ftseoul.visitor.encrypt.Seed;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckInLogDto implements Serializable {
    private List<CheckInVisitor> checkInLogs;
    private int lastPage;

    @JsonIgnore
    private boolean decrypted = false;

    public CheckInLogDto(List<CheckInVisitor> checkInLogs, int lastPage) {
        this.checkInLogs = checkInLogs;
        this.lastPage = lastPage;
    }

    public CheckInLogDto decrypt(Seed seed) {
        if (!decrypted) {
            this.checkInLogs = checkInLogs
                .stream()
                .map(checkInVisitor ->
                    CheckInVisitorDecrypt.builder()
                        .checkInDate(checkInVisitor.getCheckInDate())
                        .checkIn(checkInVisitor.getCheckIn())
                        .name(seed.decrypt(checkInVisitor.getName()))
                        .phone(seed.decrypt(checkInVisitor.getPhone()))
                        .build())
                .collect(Collectors.toList());
            decrypted = true;
        }
        return this;
    }
}