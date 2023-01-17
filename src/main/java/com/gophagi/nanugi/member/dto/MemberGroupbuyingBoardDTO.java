package com.gophagi.nanugi.member.dto;

import com.gophagi.nanugi.groupbuying.dto.GroupbuyingBoardDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberGroupbuyingBoardDTO {
    private MemberDTO memberDTO;
    private List<GroupbuyingBoardDTO> groupbuyingBoardDTOList;

    @Builder
    public MemberGroupbuyingBoardDTO(MemberDTO memberDTO, List<GroupbuyingBoardDTO> groupbuyingBoardDTOList) {
        this.memberDTO = memberDTO;
        this.groupbuyingBoardDTOList = groupbuyingBoardDTOList;
    }

}
