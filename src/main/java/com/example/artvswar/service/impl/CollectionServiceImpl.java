package com.example.artvswar.service.impl;

import com.example.artvswar.dto.mapper.CollectionMapper;
import com.example.artvswar.dto.request.collection.CollectionCreateUpdateRequestDto;
import com.example.artvswar.dto.response.collection.CollectionShortResponseDto;
import com.example.artvswar.exception.AppEntityNotFoundException;
import com.example.artvswar.model.Collection;
import com.example.artvswar.model.Painting;
import com.example.artvswar.repository.CollectionRepository;
import com.example.artvswar.service.AuthorService;
import com.example.artvswar.service.CollectionService;
import com.example.artvswar.service.PaintingService;
import com.example.artvswar.util.PrettyIdCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final PrettyIdCreator prettyIdCreator;
    private final AuthorService authorService;
    private final PaintingService paintingService;
    private final CollectionMapper collectionMapper;

    @Override
    @Transactional
    public CollectionShortResponseDto save(CollectionCreateUpdateRequestDto dto, String cognitoSubject) {
        Collection collection = collectionMapper.toModel(dto);
        collection.setPrettyId(creatPrettyId(dto.getTitle()));
        collection.setAuthor(authorService.getAuthorByCognitoSubject(cognitoSubject));
        Collection savedCollection = collectionRepository.save(collection);
        return collectionMapper.toDto(savedCollection);
    }

    @Override
    @Transactional
    public CollectionShortResponseDto update(CollectionCreateUpdateRequestDto dto, String prettyId,
                                             String cognitoSubject) {
        Collection collection = collectionRepository.findByPrettyId(prettyId).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find collection with id: %s", prettyId)));
        if (collection.getAuthor().getCognitoSubject().equals(cognitoSubject)) {
            if (!collection.getTitle().equals(dto.getTitle())) {
                collection.setPrettyId(creatPrettyId(dto.getTitle()));
            }
            Collection udatedCollection = collectionMapper.toModel(collection, dto);
            return collectionMapper.toDto(udatedCollection);
        } else {
            throw new RuntimeException(
                    String.format("Author with cognitoSubject: %s is not allowed "
                            + "to update collection with prettyId: %s", cognitoSubject, prettyId));
        }
    }

    @Override
    public CollectionShortResponseDto findByPrettyId(String collectionPrettyId) {
        return collectionRepository.getByPrettyId(CollectionShortResponseDto.class,
                collectionPrettyId);
    }

    @Override
    public Page<CollectionShortResponseDto> getAllByAuthorByPrettyId(String prettyId,
                                                                     Pageable pageable) {
        return collectionRepository.findAllByAuthorPrettyId(CollectionShortResponseDto.class,
                prettyId, pageable);
    }

    @Override
    public Page<CollectionShortResponseDto> getAllByAuthorByCognitoSubject(String cognitoSubject,
                                                                           Pageable pageable) {
        return collectionRepository.findAllByAuthorCognitoSubject(CollectionShortResponseDto.class,
                cognitoSubject, pageable);
    }

    @Override
    @Transactional
    public void addPaintingsToCollection(String collectionPrettyId, List<Long> paintingIds) {
        Collection collection = collectionRepository.findByPrettyId(collectionPrettyId).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find collection by prettyId: %s", collectionPrettyId)));
        paintingIds.forEach(id -> collection.addPainting(paintingService.getReference(id)));
    }

    @Override
    @Transactional
    public void removePaintingsFromCollection(String collectionPrettyId, List<Long> paintingIds) {
        Collection collection = collectionRepository.findByPrettyId(collectionPrettyId).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find collection by prettyId: %s", collectionPrettyId)));
        paintingIds.forEach(id -> collection.remove(paintingService.getReference(id)));
    }

    @Override
    @Transactional
    public void delete(String collectionPrettyId) {
        Collection collection = collectionRepository.findByPrettyId(collectionPrettyId).orElseThrow(
                () -> new AppEntityNotFoundException(
                        String.format("Can't find collection by prettyId: %s", collectionPrettyId)));
        Set<Painting> paintings = collection.getPaintings();
        paintings.forEach(collection::remove);
        collectionRepository.deleteByPrettyId(collectionPrettyId);
    }

    private String creatPrettyId(String title) {
        String proposedPrettyId = prettyIdCreator.create(title);
        int repentance = collectionRepository.checkIfExistPrettyId(proposedPrettyId);
        if (repentance == 0) {
            return proposedPrettyId;
        } else {
            return proposedPrettyId + "-" + new Random().nextInt(100000);
        }
    }
}
