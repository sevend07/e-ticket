package com.example.demo.DTO.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
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

    @NotNull(message = "Estimated trip duration is required")
    @Min(value = 1, message = "Trip duration must be at least one hour")
    private Integer estimatedDuration;
    
    @NotNull(message = "Estimated break duration is required")
    @Min(value = 1, message = "Break uration must be at least one hour")
    private Integer turnaroundBufferDuration;

    @NotEmpty(message = "Fleet and schedule is required")
    @Valid
    private List<FleetScheduleRequest> scheduledFleets;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FleetScheduleRequest {
        @NotNull(message = "Departure time is required")
        @Future(message = "Departure time must be in the future")
        private LocalDateTime departureSchedule;

        @NotBlank(message = "Select at least one fleet")
        private Integer fleetId;
    }

}
