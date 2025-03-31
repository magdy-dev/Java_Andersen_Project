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
 * Controller for managing workspaces within the application.
 * This controller handles requests related to workspaces, including:
 * <ul>
 *     <li>Retrieving the list of all workspaces</li>
 *     <li>Creating new workspaces</li>
 *     <li>Editing existing workspaces</li>
 *     <li>Deleting workspaces</li>
 *     <li>Retrieving available workspaces for specified time periods</li>
 * </ul>
 */
@Controller
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    /**
     * Retrieves all workspaces and displays them in a list.
     *
     * @param model the model to add attributes for the view.
     * @return the name of the view that displays the list of workspaces, or an error view if an exception occurs.
     */
    @GetMapping
    public String getAllWorkspaces(Model model) {
        try {
            List<Workspace> workspaces = workspaceService.getAllWorkspaces();
            model.addAttribute("workspaces", workspaces);
            return "workspaceList"; // Return the view for listing workspaces
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Return to an error view
        }
    }

    /**
     * Displays the form for creating a new workspace.
     *
     * @param model the model to add attributes for the view.
     * @return the name of the view that displays the workspace creation form.
     */
    @GetMapping("/create")
    public String showCreateWorkspaceForm(Model model) {
        model.addAttribute("workspace", new Workspace()); // Prepare a new Workspace object for the form
        return "createWorkspace"; // Return the view for creating a workspace
    }

    /**
     * Handles the creation of a new workspace.
     *
     * @param workspace the workspace details provided by the user.
     * @param model the model to add attributes for the view.
     * @return a redirect to the list of workspaces if the creation is successful;
     *         otherwise returns to the workspace creation form with an error message.
     */
    @PostMapping("/create")
    public String createWorkspace(@ModelAttribute Workspace workspace, Model model) {
        try {
            workspaceService.createWorkspace(workspace);
            return "redirect:/workspaces"; // Redirect to the list of workspaces after successful creation
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "createWorkspace"; // Return to the form with an error message
        }
    }

    /**
     * Displays the form for editing an existing workspace.
     *
     * @param id the ID of the workspace to edit.
     * @param model the model to add attributes for the view.
     * @return the name of the view that displays the workspace edit form, or an error view if an exception occurs.
     */
    @GetMapping("/edit/{id}")
    public String showEditWorkspaceForm(@PathVariable Long id, Model model) {
        try {
            Workspace workspace = workspaceService.getWorkspaceById(id);
            model.addAttribute("workspace", workspace);
            return "editWorkspace"; // Return the view for editing a workspace
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Return to an error view
        }
    }

    /**
     * Handles the update of an existing workspace.
     *
     * @param workspace the updated workspace details provided by the user.
     * @param model the model to add attributes for the view.
     * @return a redirect to the list of workspaces if the update is successful;
     *         otherwise returns to the edit form with an error message.
     */
    @PostMapping("/edit")
    public String updateWorkspace(@ModelAttribute Workspace workspace, Model model) {
        try {
            if (workspaceService.updateWorkspace(workspace)) {
                return "redirect:/workspaces"; // Redirect to the list of workspaces after successful update
            } else {
                model.addAttribute("error", "Workspace not found");
                return "editWorkspace"; // Return to the edit form with an error message
            }
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "editWorkspace"; // Return to the edit form with an error message
        }
    }

    /**
     * Handles the deletion of an existing workspace.
     *
     * @param id the ID of the workspace to delete.
     * @param model the model to add attributes for the view.
     * @return a redirect to the list of workspaces if the deletion is successful;
     *         otherwise returns to an error view with an error message.
     */
    @PostMapping("/delete/{id}")
    public String deleteWorkspace(@PathVariable Long id, Model model) {
        try {
            if (workspaceService.deleteWorkspace(id)) {
                return "redirect:/workspaces"; // Redirect to the list of workspaces after successful deletion
            } else {
                model.addAttribute("error", "Workspace not found");
                return "error"; // Return to an error view
            }
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Return to an error view
        }
    }

    /**
     * Retrieves available workspaces within a specified time range.
     *
     * @param startTime the start time of the booking period.
     * @param endTime the end time of the booking period.
     * @param model the model to add attributes for the view.
     * @return the name of the view that displays available workspaces,
     *         or an error view if an exception occurs.
     */
    @GetMapping("/available")
    public String getAvailableWorkspaces(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime, Model model) {
        try {
            List<Workspace> availableWorkspaces = workspaceService.getAvailableWorkspaces(startTime, endTime);
            model.addAttribute("availableWorkspaces", availableWorkspaces);
            return "availableWorkspaces"; // Return the view for displaying available workspaces
        } catch (WorkspaceServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Return to an error view
        }
    }
}