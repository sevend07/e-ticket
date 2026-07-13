package com.example.demo.DTO.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTripRequest {

    @NotNull(message = "Departure terminal is required")
    private Integer departureTerminalId;

    @NotNull(message = "Destination terminal is required")
    private Integer destinationTerminalId;

    @NotNull(message = "Estimated duration is required")
    private Integer estimatedDuration;

    @NotNull(message = "Estimated break duration is reqeuired")
    private Integer estimatedBreakDuration;

    @NotEmpty(message = "Fleet and schedule is required")
    @Valid
    private List<FleetScheduleRequest> scheduledFleets;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FleetScheduleRequest {
        @NotNull
        private LocalDateTime departure;

        @NotNull
        private LocalDateTime arrival;

        @NotBlank
        private Set<Integer> fleetIds;
    }

}
