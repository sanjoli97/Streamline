import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { NgSelectModule } from "@ng-select/ng-select";
import { UserDTO } from 'src/app/shared/UserDTO';
import { UserService } from 'src/app/services/user.service';
import { ProjectService } from 'src/app/services/project.service';
import { ProjectDTO } from 'src/app/shared/ProjectDTO';

@Component({
  selector: 'app-edit-project',
  templateUrl: './edit-project.component.html',
  styleUrls: ['./edit-project.component.css']
})
export class EditProjectComponent implements OnInit {
  users: Array<UserDTO>;
  editProjectForm: FormGroup;
  selected: any;
  projectDTO: ProjectDTO;

  constructor(private userService: UserService,
    private projectService: ProjectService) { 
      this.projectDTO = {
        projectName: '',
        users: [],
        projectId: '',
        tickets: []
      };
    }

  ngOnInit(): void {
    this.editProjectForm = new FormGroup({
      projectName: new FormControl('', Validators.required),
      users: new FormControl('', Validators.required)
    });
    this.userService.getAllUsers().subscribe(res => {
      this.users = res;
    });
  }

  submit() {
    debugger
    this.projectDTO.projectName = this.editProjectForm.value.projectName;
    this.projectDTO.users = this.editProjectForm.value.users;

    this.projectService.createProject(this.projectDTO).subscribe(res => {
      debugger;
      console.log(res);
    })
    console.log(this);
  }
}
