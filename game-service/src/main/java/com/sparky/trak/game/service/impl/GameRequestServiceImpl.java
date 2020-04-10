package com.sparky.trak.game.service.impl;

import com.sparky.trak.game.domain.GameRequest;
import com.sparky.trak.game.repository.GameRequestRepository;
import com.sparky.trak.game.service.AuthenticationService;
import com.sparky.trak.game.service.GameRequestService;
import com.sparky.trak.game.service.PatchService;
import com.sparky.trak.game.service.dto.GameRequestDto;
import com.sparky.trak.game.service.exception.InvalidUserException;
import com.sparky.trak.game.service.mapper.GameRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.json.JsonMergePatch;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameRequestServiceImpl implements GameRequestService {

    private final GameRequestRepository gameRequestRepository;
    private final GameRequestMapper gameRequestMapper;
    private final AuthenticationService authenticationService;
    private final MessageSource messageSource;
    private final PatchService patchService;

    @Override
    public GameRequestDto save(GameRequestDto gameRequestDto) {
        Objects.requireNonNull(gameRequestDto);

        if (!authenticationService.isCurrentAuthenticatedUser(gameRequestDto.getUserId())) {
            String errorMessage = messageSource
                    .getMessage("game-request.exception.invalid-user", new Object[] {}, LocaleContextHolder.getLocale());

            throw new InvalidUserException(errorMessage);
        }

        if (gameRequestRepository.existsById(gameRequestDto.getId())) {
            String errorMessage = messageSource
                    .getMessage("game-request.exception.entity-exists", new Object[] { gameRequestDto.getId() }, LocaleContextHolder.getLocale());

            throw new EntityExistsException(errorMessage);
        }

        return gameRequestMapper.gameRequestToGameRequestDto(gameRequestRepository.save(gameRequestMapper.gameRequestDtoToGameRequest(gameRequestDto)));
    }

    @Override
    public GameRequestDto findById(long id) {
        String errorMessage = messageSource
                .getMessage("game-request.exception.not-found", new Object[] { id }, LocaleContextHolder.getLocale());

        return gameRequestMapper.gameRequestToGameRequestDto(gameRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(errorMessage)));
    }

    @Override
    public Iterable<GameRequestDto> findAll(Pageable pageable) {
        Objects.requireNonNull(pageable);

        return gameRequestRepository.findAll(pageable)
                .map(gameRequestMapper::gameRequestToGameRequestDto);
    }

    @Override
    public GameRequestDto update(GameRequestDto gameRequestDto) {
        Objects.requireNonNull(gameRequestDto);

        if (!authenticationService.isCurrentAuthenticatedUser(gameRequestDto.getUserId())) {
            String errorMessage = messageSource
                    .getMessage("game-request.exception.invalid-user", new Object[] {}, LocaleContextHolder.getLocale());

            throw new InvalidUserException(errorMessage);
        }

        if (!gameRequestRepository.existsById(gameRequestDto.getId())) {
            String errorMessage = messageSource
                    .getMessage("game-request.exception.not-found", new Object[] { gameRequestDto.getId() }, LocaleContextHolder.getLocale());

            throw new EntityNotFoundException(errorMessage);
        }

        return gameRequestMapper.gameRequestToGameRequestDto(gameRequestRepository.save(gameRequestMapper.gameRequestDtoToGameRequest(gameRequestDto)));
    }

    @Override
    public GameRequestDto patch(long id, JsonMergePatch jsonMergePatch) {
        // Set the new Java object with the patch information.
        GameRequestDto patched = patchService.patch(jsonMergePatch, findById(id), GameRequestDto.class);

        if (!authenticationService.isCurrentAuthenticatedUser(patched.getUserId())) {
            String errorMessage = messageSource
                    .getMessage("game-request.exception.invalid-user", new Object[] {}, LocaleContextHolder.getLocale());

            throw new InvalidUserException(errorMessage);
        }

        // Save to the repository and convert it back to a GameDto.
        return gameRequestMapper.gameRequestToGameRequestDto(gameRequestRepository.save(gameRequestMapper.gameRequestDtoToGameRequest(patched)));
    }

    @Override
    public void deleteById(long id) {
        Optional<GameRequest> gameRequest = gameRequestRepository.findById(id);

        if (!gameRequest.isPresent()) {
            String errorMessage = messageSource
                    .getMessage("game-request.exception.not-found", new Object[] { id }, LocaleContextHolder.getLocale());

            throw new EntityNotFoundException(errorMessage);
        }

        if (!authenticationService.isCurrentAuthenticatedUser(gameRequest.get().getUserId())) {
            String errorMessage = messageSource
                    .getMessage("game-request.exception.invalid-user", new Object[] {}, LocaleContextHolder.getLocale());

            throw new InvalidUserException(errorMessage);
        }

        gameRequestRepository.deleteById(id);
    }
}
