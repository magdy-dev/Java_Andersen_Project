package com.andersen.domain.mapper;

import com.andersen.domain.dto.workspace.WorkspaceDto;
import com.andersen.domain.entity.workspace.Workspace;

public class WorkspaceMapper {

    public static WorkspaceDto toDto(Workspace entity) {
        if (entity == null) return null;

        return new WorkspaceDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getPricePerHour(),
                entity.getCapacity(),
                entity.isActive()
        );
    }

    public static Workspace toEntity(WorkspaceDto dto) {
        if (dto == null) return null;

        return new Workspace(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getType(),
                dto.getPricePerHour(),
                dto.getCapacity(),
                dto.isActive()
        );
    }
}
