package com.codingrecipe.board.controller;

import com.codingrecipe.board.dto.BoardDTO;
import com.codingrecipe.board.dto.BoardFileDTO;
import com.codingrecipe.board.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/save")
    public String save() {
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        boardService.save(boardDTO);
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board", boardDTO);
        if(boardDTO.getFileAttached() == 1){
            List<BoardFileDTO> boardFileDTO = boardService.findFile(id);
            model.addAttribute("boardFileList", boardFileDTO);
        }
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String findByOne(@PathVariable("id") Long id, Model model) {
        BoardDTO boardDTO =boardService.findById(id);
        model.addAttribute("board", boardDTO);
        if(boardDTO.getFileAttached()==1){
            List<BoardFileDTO> boardFileDTO = boardService.findFile(boardDTO.getId());
            model.addAttribute("boardFileList", boardFileDTO);
        }
        return "update";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute BoardDTO boardDTO, @RequestParam("imageDel") Long[] ckId, Model model) {
        if (ckId != null) {
            //System.out.println(Arrays.toString(ckId));
            for (Long fileId : ckId) {
                BoardFileDTO boardFileDTO = boardService.findByFileId(fileId);
                String saveUrl = "D:/Dev/codingRecipe/upload_files/" + boardFileDTO.getStoredFileName();
                File fileToDelete = new File(saveUrl);
                if (fileToDelete.delete()) {
                    boardService.deleteFile(fileId);
                } else {
                    // 파일 삭제 실패 또는 파일이 존재하지 않음
                }
            }
        }
        boardService.update(boardDTO);
        BoardDTO boardDetail = boardService.findById(boardDTO.getId());
        model.addAttribute("board", boardDetail);
        return "detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return "redirect:/list";
    }
}
