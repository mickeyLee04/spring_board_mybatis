package com.codingrecipe.board.service;

import com.codingrecipe.board.dto.BoardDTO;
import com.codingrecipe.board.dto.BoardFileDTO;
import com.codingrecipe.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
   private final BoardRepository boardRepository;

   public void save(BoardDTO boardDTO) throws IOException {
      if (boardDTO.getBoardFile().get(0).isEmpty()) {
         boardDTO.setFileAttached(0);
         boardRepository.save(boardDTO);
      } else {
         boardDTO.setFileAttached(1);
         BoardDTO dtoId = boardRepository.save(boardDTO);
         for(MultipartFile boardFile : boardDTO.getBoardFile()) {
            String originalFilename = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "-" + originalFilename;

            String saveUrl = "D:/Dev/codingRecipe/upload_files/" + storedFileName;
            boardFile.transferTo(new File(saveUrl));

            BoardFileDTO boardFileDTO = new BoardFileDTO();
            boardFileDTO.setOriginalFileName(originalFilename);
            boardFileDTO.setStoredFileName(storedFileName);
            boardFileDTO.setBoardId(dtoId.getId());
            boardRepository.saveFile(boardFileDTO);
         }

      }

   }

   public List<BoardDTO> findAll() {
      return boardRepository.findAll();
   }

   public void updateHits(Long id) {
      boardRepository.updateHits(id);
   }

   public BoardDTO findById(Long id) {
      return boardRepository.findById(id);
   }


   public void update(BoardDTO boardDTO) {
      boardRepository.update(boardDTO);
   }

   public void delete(Long id) {
      boardRepository.delete(id);
   }

   public List<BoardFileDTO> findFile(Long boardId) {
      return boardRepository.findFile(boardId);
   }

   public BoardFileDTO findByFileId(Long fileId) {
      return boardRepository.findByFileId(fileId);
   }

   public void deleteFile(Long fileId) {
     boardRepository.deleteFile(fileId);
   }
}
