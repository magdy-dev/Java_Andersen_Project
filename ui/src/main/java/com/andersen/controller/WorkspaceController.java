package com.andersen.controller;

import com.andersen.entity.workspace.Workspace;
import com.andersen.service.exception.WorkspaceServiceException;
import com.andersen.service.workspace.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for handling workspace management operations.
 * This class provides endpoints for viewing, creating, editing,
 * deleting workspaces, and retrieving available workspaces.
 */
@Controller
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * Constructs a WorkspaceController with the specified WorkspaceService.
     *
     * @param workspaceService the WorkspaceService used for managing workspaces.
     */
    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * Retrieves all workspaces and prepares them for display.
     *
     * @param model the model to hold attributes for the view.
     * @return the view name for displaying the list of workspaces.
     */
    @GetMapping
    public String getAllWorkspaces(Model model) {
        try {
            List<Workspace> workspaces = workspaceService.getAllWorkspaces();
            model.addAttribute("workspaces", workspaces);
            return "workspaceList"; // Remove .jsp extension
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * Displays the form for creating a new workspace.
     *
     * @param model the model to hold attributes for the view.
     * @return the view name for the create workspace form.
     */
    @GetMapping("/create")
    public String showCreateWorkspaceForm(Model model) {
        model.addAttribute("workspace", new Workspace());
        return "createWorkspace";
    }

    /**
     * Processes the creation of a new workspace.
     *
     * @param workspace the Workspace object containing workspace details.
     * @param model the model to hold attributes for the view.
     * @return a redirect URL depending on the outcome of the workspace creation.
     */
    @PostMapping("/create")
    public String createWorkspace(@ModelAttribute Workspace workspace, Model model) {
        try {
            workspaceService.createWorkspace(workspace);
            return "redirect:/workspaces";
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("workspace", workspace); // Preserve form input
            return "createWorkspace";
        }
    }

    /**
     * Displays the form for editing an existing workspace.
     *
     * @param id the ID of the workspace to be edited.
     * @param model the model to hold attributes for the view.
     * @return the view name for the edit workspace form.
     */
    @GetMapping("/edit/{id}")
    public String showEditWorkspaceForm(@PathVariable Long id, Model model) {
        try {
            Workspace workspace = workspaceService.getWorkspaceById(id);
            model.addAttribute("workspace", workspace);
            return "editWorkspace";
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * Processes the update of an existing workspace.
     *
     * @param workspace the Workspace object containing updated workspace details.
     * @param model the model to hold attributes for the view.
     * @return a redirect URL depending on the outcome of the workspace update.
     */
    @PostMapping("/edit")
    public String updateWorkspace(@ModelAttribute Workspace workspace, Model model) {
        try {
            workspaceService.updateWorkspace(workspace);
            return "redirect:/workspaces";
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("workspace", workspace); // Preserve form input
            return "editWorkspace";
        }
    }

    /**
     * Processes the deletion of an existing workspace.
     *
     * @param id the ID of the workspace to be deleted.
     * @param model the model to hold attributes for the view.
     * @return a redirect URL depending on the outcome of the workspace deletion.
     */
    @PostMapping("/delete/{id}")
    public String deleteWorkspace(@PathVariable Long id, Model model) {
        try {
            workspaceService.deleteWorkspace(id);
            return "redirect:/workspaces";
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * Retrieves a list of available workspaces for a specified time period.
     *
     * @param startTime the start time of the desired period for workspace availability.
     * @param endTime the end time of the desired period for workspace availability.
     * @param model the model to hold attributes for the view.
     * @return the view name for displaying available workspaces.
     */
    @GetMapping("/available")
    public String getAvailableWorkspaces(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            Model model) {
        try {
            List<Workspace> availableWorkspaces = workspaceService.getAvailableWorkspaces(startTime, endTime);
            model.addAttribute("availableWorkspaces", availableWorkspaces);
            model.addAttribute("startTime", startTime);
            model.addAttribute("endTime", endTime);
            return "availableWorkspaces";
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}