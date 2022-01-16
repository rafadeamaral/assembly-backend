package com.amaral.assembly.associate.service;

import com.amaral.assembly.associate.domain.Associate;
import com.amaral.assembly.associate.domain.AssociateDTO;
import com.amaral.assembly.associate.domain.AssociateStatus;
import com.amaral.assembly.associate.repository.AssociateRepository;
import com.amaral.assembly.common.exception.DataIntegratyViolationException;
import com.amaral.assembly.common.exception.ObjectNotFoundException;
import com.amaral.assembly.common.exception.ServiceException;
import com.amaral.assembly.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class AssociateService {

    @Autowired
    private AssociateRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserService userService;

    public List<AssociateDTO> findAll() {

        return repository.findAll()
                .stream().map(obj -> mapper.map(obj, AssociateDTO.class)).collect(Collectors.toList());
    }

    public AssociateDTO findById(Integer id) {

        Optional<Associate> optional = repository.findById(id);

        Associate entity = optional.orElseThrow(() -> new ObjectNotFoundException("associate.not.found"));

        return mapper.map(entity, AssociateDTO.class);
    }

    public AssociateDTO create(AssociateDTO obj) {

        validateCpf(obj);

        obj.setStatus(AssociateStatus.ACTIVE);

        return save(obj);
    }

    public AssociateDTO update(AssociateDTO obj) {

        AssociateDTO dto = findById(obj.getId());

        validateCpfUpdate(obj, dto);

        obj.setCpf(dto.getCpf());

        return save(obj);
    }

    private void validateCpfUpdate(AssociateDTO obj, AssociateDTO dto) {

        if (!isNull(obj.getCpf()) && !obj.getCpf().equals(dto.getCpf())) {

            throw new ServiceException("cpf.cannot.be.changed");
        }
    }

    private AssociateDTO save(AssociateDTO obj) {

        Associate entity = mapper.map(obj, Associate.class);

        entity = repository.save(entity);

        return mapper.map(entity, AssociateDTO.class);
    }

    private void validateCpf(AssociateDTO obj) {

        Optional<Associate> optional = repository.findByCpf(obj.getCpf());

        if (optional.isPresent() && !optional.get().getId().equals(obj.getId())) {

            throw new DataIntegratyViolationException("cpf.already.registered");
        }

        userService.validateCpf(obj.getCpf());
    }

    public void validateVote(Integer id) {

        AssociateDTO obj = findById(id);

        if (AssociateStatus.INACTIVE.equals(obj.getStatus())) {

            throw new ServiceException("inactive.associate");
        }

        userService.validateVote(obj.getCpf());
    }

}
